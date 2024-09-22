package tech.kafka.kafkaConsumerApp.config;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import tech.kafka.kafkaConsumerApp.model.Product;

@Log4j2
public class ProductDeserializer implements Deserializer<Product> {

  private final ObjectMapper objectMapper = new ObjectMapper ();

  @Override
  public void configure (Map<String, ?> configs, boolean isKey) {
    // Configuration if needed
  }

  @Override
  public Product deserialize (String topic, byte[] data) {
    if (data == null) {
      return null;
    }

    try {
      // Deserialize JSON to a JsonNode
      var node = objectMapper.readTree (data);

      // Extract fields manually and create a Product object
      UUID productId = UUID.fromString (node.get ("productId").asText ());
      String productName = node.get ("productName").asText ();
      BigDecimal price = new BigDecimal (node.get ("price").asText ());
      String originCountry = node.get ("originCountry").asText ();

      String mfgDateString = node.get ("mfgDate").asText ();
      Instant mfgDate;
      log.info ("mfgDateString: {}", mfgDateString);
      if (mfgDateString == null || mfgDateString.isEmpty ()) {
        // Handle missing date, e.g., set to the current time
        mfgDate = Instant.now ();
      }
      else {
        try {
          double timestampDouble = Double.parseDouble (mfgDateString);
          // Separate seconds and nanoseconds
          long seconds = (long) timestampDouble;
          long nanos = (long) ((timestampDouble - seconds) * 1_000_000_000);
          mfgDate = Instant.ofEpochSecond (seconds, nanos);
        }
        catch (DateTimeParseException e) {
          log.error ("Failed to parse manufacturing date: {}", e.getMessage ());
          mfgDate = Instant.now (); // or any fallback logic
        }
      }

      // Return the deserialized Product object
      return Product.builder ()
          .productId (productId)
          .productName (productName)
          .price (price)
          .mfgDate (Instant.from (mfgDate))
          .originCountry (originCountry)
          .build ();

    }
    catch (IOException e) {
      log.warn ("Error deserializing Product: {}", e.getMessage ());
      return null;
    }
  }

  @Override
  public void close () {
    // Close resources if needed
  }
}


