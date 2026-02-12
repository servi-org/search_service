package live.servi.search.infrastructure.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.lang.NonNull;

import live.servi.search.infrastructure.adapter.input.messaging.dto.ServiceEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Configuración de Kafka Consumer
 */
@Configuration
public class KafkaConsumerConfig {
    
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.consumer.group-id:search-service-group}")
    private String groupId;
    
    @Value("${spring.kafka.properties.sasl.mechanism:PLAIN}")
    private String saslMechanism;
    
    @Value("${spring.kafka.properties.security.protocol:SASL_SSL}")
    private String securityProtocol;
    
    @Value("${spring.kafka.properties.sasl.jaas.config:}")
    private String saslJaasConfig;
    
    @Value("${spring.kafka.properties.session.timeout.ms:45000}")
    private Integer sessionTimeoutMs;
    
    /**
     * Configura la fábrica de consumidores de Kafka.
     * 
     * Define cómo el consumidor se conecta a Kafka y cómo maneja los mensajes.
     */
    @Bean
    @NonNull
    public ConsumerFactory<UUID, ServiceEvent> consumerFactory() {
        Map<String, Object> configs = new HashMap<>();
        
        //Define la direccion del clúster de Kafka al que el consumidor se conectara. el consumidor se conectará al broker en esa dirección.
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        //Define el grupo de consumidores al que pertenece este consumidor. todos los consumidores con este ID compartirán las particiones del topic.
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //Define cómo deserializar la key del mensaje. en este caso use UUIDDeserializer para convertir los bytes de la key a un objeto UUID
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        //Define cómo deserializar el value del mensaje. en este caso use JsonDeserializer para convertir los bytes del value a un objeto ServiceEvent
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        
        // Config del JsonDeserializer. 
        // Permite deserializar objetos de cualquier paquete.
        configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        //Define el tipo de objeto al que se deserializará el value. En este caso, el value se deserializa a un objeto de tipo ServiceEvent.
        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ServiceEvent.class.getName());
        
        // Si no hay offset guardado, leer desde el principio
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        // Configuración de seguridad para Confluent Cloud (SASL_SSL)
        if (saslJaasConfig != null && !saslJaasConfig.isEmpty()) {
            configs.put("sasl.mechanism", saslMechanism);
            configs.put("security.protocol", securityProtocol);
            configs.put("sasl.jaas.config", saslJaasConfig);
            configs.put("session.timeout.ms", sessionTimeoutMs);
        }
        
        return new DefaultKafkaConsumerFactory<>(
            configs,
            new UUIDDeserializer(),
            new JsonDeserializer<>(ServiceEvent.class, false)
        );
    }
    
    /**
     * Factory para crear listeners de Kafka
     * Permite procesar mensajes concurrentemente
     * ConcurrentKafkaListenerContainerFactory es una fábrica utilizada por Spring Kafka
     * para crear contenedores de listeners. Estos contenedores son responsables de 
     * escuchar mensajes de Kafka y procesarlos de manera concurrente.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, ServiceEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<UUID, ServiceEvent> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
