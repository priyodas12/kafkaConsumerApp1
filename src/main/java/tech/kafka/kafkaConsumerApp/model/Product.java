package tech.kafka.kafkaConsumerApp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class Product implements Serializable {

  @JsonProperty ("productId")
  private UUID productId;
  @JsonProperty ("productName")
  private String productName;
  @JsonProperty ("price")
  private BigDecimal price;
  @JsonProperty ("mfgDate")
  private Instant mfgDate;
  @JsonProperty ("originCountry")
  private String originCountry;
}
