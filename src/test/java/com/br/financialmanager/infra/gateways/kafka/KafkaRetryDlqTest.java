package com.br.financialmanager.infra.gateways.kafka;

import com.br.financialmanager.application.usecases.transaction.ProcessarTransacao;
import com.br.financialmanager.config.KafkaConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.math.BigDecimal;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("worker")
@EmbeddedKafka(partitions = 1, topics = { "transaction.requested", "transaction.requested.DLT" })
@Import(KafkaConfig.class)
public class KafkaRetryDlqTest {

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  @MockitoBean
  private ProcessarTransacao processarTransacao;

  @Test
  public void deveTentarReprocessarEEnviarParaDlqQuandoFalhar() {

    Mockito.doThrow(new RuntimeException("Erro simulado de conexão"))
      .when(processarTransacao)
      .executar(any(), any(), any(), any(), any(), anyBoolean());

    TransacaoEvent evento = new TransacaoEvent("id-123", "user-1",
      BigDecimal.TEN, "BRL", "SAIDA", false);

    kafkaTemplate.send("transaction.requested", "id-123", evento);

    verify(processarTransacao, timeout(10000).times(4))
      .executar(any(), any(), any(), any(), any(), anyBoolean());

    Consumer<String, String> consumerDlq = criarConsumidorDeTeste();
    embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumerDlq,
      "transaction.requested.DLT");

    ConsumerRecord<String, String> record = KafkaTestUtils
      .getSingleRecord(consumerDlq, "transaction.requested.DLT");

    Assertions.assertNotNull(record, "A mensagem deveria estar na DLQ");
    Assertions.assertTrue(record.value().contains("id-123"), "O conteúdo da mensagem na DLQ deve conter o ID original");
    consumerDlq.close();
  }

  private Consumer<String, String> criarConsumidorDeTeste() {
    Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-dlt-group", "false", embeddedKafkaBroker);
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    DefaultKafkaConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(consumerProps);
    return factory.createConsumer();
  }
}
