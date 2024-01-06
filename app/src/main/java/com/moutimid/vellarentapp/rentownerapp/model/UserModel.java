package com.moutimid.vellarentapp.rentownerapp.model;

public class UserModel {
  
        private String name;
        private String phone;
        private String email;
        private String image;
        private String ownerId;
        // Assuming you want to store some unique identifier for the owner

        // Default constructor required for DataSnapshot.getValue(UserModel.class)
        public UserModel() {
        }

        public UserModel(String name, String phone, String email, String image, String ownerId) {
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.image = image;
            this.ownerId = ownerId;
        }

        public UserModel(String name, String phone, String email, String ownerId) {
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.ownerId = ownerId;
        }

        // Getter and Setter methods for the fields

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

