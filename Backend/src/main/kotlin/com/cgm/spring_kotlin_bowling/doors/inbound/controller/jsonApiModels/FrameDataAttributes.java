package com.cgm.spring_kotlin_bowling.doors.inbound.controller.jsonApiModels;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

/**
 * FrameDataAttributes
 */
@Validated
public class FrameDataAttributes   {
  @JsonProperty("shot1")
  private Integer shot1 = null;

  @JsonProperty("shot2")
  private Integer shot2 = null;

  @JsonProperty("shot3")
  private Integer shot3 = null;

  @JsonProperty("flag")
  private String flag = null;

  @JsonProperty("score")
  private Integer score = null;

  public FrameDataAttributes shot1(Integer shot1) {
    this.shot1 = shot1;
    return this;
  }

  /**
   * Get shot1
   * @return shot1
  **/
  @ApiModelProperty(value = "")

  public Integer getShot1() {
    return shot1;
  }

  public void setShot1(Integer shot1) {
    this.shot1 = shot1;
  }

  public FrameDataAttributes shot2(Integer shot2) {
    this.shot2 = shot2;
    return this;
  }

  /**
   * Get shot2
   * @return shot2
  **/
  @ApiModelProperty(value = "")

  public Integer getShot2() {
    return shot2;
  }

  public void setShot2(Integer shot2) {
    this.shot2 = shot2;
  }

  public FrameDataAttributes shot3(Integer shot3) {
    this.shot3 = shot3;
    return this;
  }

  /**
   * Get shot3
   * @return shot3
  **/
  @ApiModelProperty(value = "")

  public Integer getShot3() {
    return shot3;
  }

  public void setShot3(Integer shot3) {
    this.shot3 = shot3;
  }

  public FrameDataAttributes flag(String flag) {
    this.flag = flag;
    return this;
  }

  /**
   * Get flag
   * @return flag
  **/
  @ApiModelProperty(value = "")

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public FrameDataAttributes score(Integer score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
  **/
  @ApiModelProperty(value = "")

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FrameDataAttributes frameDataAttributes = (FrameDataAttributes) o;
    return Objects.equals(this.shot1, frameDataAttributes.shot1) &&
        Objects.equals(this.shot2, frameDataAttributes.shot2) &&
        Objects.equals(this.shot3, frameDataAttributes.shot3) &&
        Objects.equals(this.flag, frameDataAttributes.flag) &&
        Objects.equals(this.score, frameDataAttributes.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shot1, shot2, shot3, flag, score);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FrameDataAttributes {\n");
    
    sb.append("    shot1: ").append(toIndentedString(shot1)).append("\n");
    sb.append("    shot2: ").append(toIndentedString(shot2)).append("\n");
    sb.append("    shot3: ").append(toIndentedString(shot3)).append("\n");
    sb.append("    flag: ").append(toIndentedString(flag)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
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
