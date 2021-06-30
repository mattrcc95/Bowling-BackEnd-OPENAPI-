package com.cgm.spring_kotlin_bowling.jsonApiModels;

import java.util.Objects;
import com.cgm.spring_kotlin_bowling.jsonApiModels.FrameData;
import com.cgm.spring_kotlin_bowling.jsonApiModels.FrameLinks;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Frame with Link included
 */
@ApiModel(description = "Frame with Link included")
@Validated
public class Frame   {
  @JsonProperty("links")
  private FrameLinks links = null;

  @JsonProperty("data")
  private FrameData data = null;

  public Frame links(FrameLinks links) {
    this.links = links;
    return this;
  }

  /**
   * Get links
   * @return links
  **/
  @ApiModelProperty(value = "")

  @Valid
  public FrameLinks getLinks() {
    return links;
  }

  public void setLinks(FrameLinks links) {
    this.links = links;
  }

  public Frame data(FrameData data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public FrameData getData() {
    return data;
  }

  public void setData(FrameData data) {
    this.data = data;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Frame frame = (Frame) o;
    return Objects.equals(this.links, frame.links) &&
        Objects.equals(this.data, frame.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Frame {\n");
    
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
