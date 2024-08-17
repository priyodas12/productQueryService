package tech.springboot.productQueryService.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table (name = "read_products_events")
public class SqsEvent {

  @Id
  @GeneratedValue (strategy = GenerationType.UUID)
  private UUID eventId;

  @JsonProperty ("eventType")
  private String eventType;

  @JsonProperty ("eventPayload")
  private String eventPayload;

  @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "America/Phoenix")
  private Date create_timestamp;
}
