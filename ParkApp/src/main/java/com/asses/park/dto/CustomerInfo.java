package com.asses.park.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerInfo implements Serializable{
    @Min(value = 1,message = "ssNumber is always starts with positive number and greater than 1")
    @NotNull(message = "ssNumber should be given as number E.g 1234")
    private Long ssNumber;

    @NotBlank(message = "email should not be blank")
    @Email(message = "Please provide a valid email E.g abcd@gmail.com",regexp=".+@.+\\..+")
    private String email;

    //@NotBlank(message = "fullName should not be blank")
    private String fullName;

}
