package com.cgm.spring_kotlin_bowling.jsonApiModels;

import java.util.Objects;
import com.cgm.spring_kotlin_bowling.jsonApiModels.Frame;
import com.cgm.spring_kotlin_bowling.jsonApiModels.FrameLinks;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * real-time scoreboard, formed by noLinkFrame items
 */
@ApiModel(description = "real-time scoreboard, formed by noLinkFrame items")
@Validated
public class Scoreboard   {
  @JsonProperty("links")
  private FrameLinks links = null;

  @JsonProperty("data")
  @Valid
  private List<Frame> data = new ArrayList<>();

  public Scoreboard links(FrameLinks links) {
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

  public Scoreboard data(List<Frame> data) {
    this.data = data;
    return this;
  }

  public Scoreboard addDataItem(Frame dataItem) {
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public List<Frame> getData() {
    return data;
  }

  public void setData(List<Frame> data) {
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
    Scoreboard scoreboard = (Scoreboard) o;
    return Objects.equals(this.links, scoreboard.links) &&
        Objects.equals(this.data, scoreboard.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Scoreboard {\n");
    
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
