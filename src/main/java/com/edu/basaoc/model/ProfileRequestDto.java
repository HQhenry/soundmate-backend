package com.edu.basaoc.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequestDto {
        private String name;
        private long age;
        private String contactInfo;
        private String bio;
        private GenderType genderType;
        private Double latitude;
        private Double longitude;

}
