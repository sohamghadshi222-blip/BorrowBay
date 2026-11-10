package com.borrowbay.borrowbay.controller;

import com.borrowbay.borrowbay.entity.User;
import com.borrowbay.borrowbay.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    // =========================================
    // REGISTER
    // =========================================

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User user) {

        try {

            if (user.getName() == null ||
                    user.getName().trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Name is required.");
            }


            if (user.getEmail() == null ||
                    user.getEmail().trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Email is required.");
            }


            if (user.getPassword() == null ||
                    user.getPassword().trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Password is required.");
            }


            User existingUser =
                    userService.findByEmail(
                            user.getEmail()
                    );


            if (existingUser != null) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Email already registered.");
            }


            User savedUser =
                    userService.register(user);


            /*
             * Do not return password to frontend.
             */

            savedUser.setPassword(null);


            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedUser);


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed.");
        }
    }



    // =========================================
    // LOGIN
    // =========================================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {

            if (request.getEmail() == null ||
                    request.getPassword() == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Email and password are required.");
            }


            User user =
                    userService.findByEmail(
                            request.getEmail()
                    );


            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password.");
            }


            boolean passwordCorrect =
                    userService.checkPassword(
                            request.getPassword(),
                            user.getPassword()
                    );


            if (!passwordCorrect) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password.");
            }


            /*
             * Do not send encrypted password
             * to frontend.
             */

            user.setPassword(null);


            return ResponseEntity.ok(user);


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed.");
        }
    }



    // =========================================
    // GET PROFILE
    // =========================================

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @RequestParam String email) {

        try {

            User user =
                    userService.findByEmail(email);


            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }


            user.setPassword(null);


            return ResponseEntity.ok(user);


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to load profile.");
        }
    }



    // =========================================
    // UPDATE PROFILE
    // =========================================

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestParam String oldEmail,
            @RequestBody User updatedUser) {

        try {

            User existingUser =
                    userService.findByEmail(oldEmail);


            if (existingUser == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }


            // -----------------------------
            // NAME
            // -----------------------------

            if (updatedUser.getName() != null &&
                    !updatedUser.getName()
                            .trim()
                            .isEmpty()) {

                existingUser.setName(
                        updatedUser.getName().trim()
                );
            }


            // -----------------------------
            // EMAIL
            // -----------------------------

            if (updatedUser.getEmail() != null &&
                    !updatedUser.getEmail()
                            .trim()
                            .isEmpty()) {

                String newEmail =
                        updatedUser.getEmail()
                                .trim();


                /*
                 * Check if another account
                 * already uses this email.
                 */

                User emailUser =
                        userService.findByEmail(
                                newEmail
                        );


                if (emailUser != null &&
                        !emailUser.getId()
                                .equals(existingUser.getId())) {

                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body("Email already registered.");
                }


                existingUser.setEmail(newEmail);
            }


            // -----------------------------
            // PHONE
            // -----------------------------

            if (updatedUser.getPhone() != null) {

                String phone =
                        updatedUser.getPhone().trim();


                /*
                 * Empty phone is allowed.
                 * Otherwise save the number.
                 */

                existingUser.setPhone(phone);
            }


            // -----------------------------
            // SAVE
            // -----------------------------

            User savedUser =
                    userService.updateUser(
                            existingUser
                    );


            /*
             * Never return password.
             */

            savedUser.setPassword(null);


            return ResponseEntity.ok(savedUser);


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to update profile.");
        }
    }



    // =========================================
    // CHANGE PASSWORD
    // =========================================

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody PasswordChangeRequest request) {

        try {

            if (request.getEmail() == null ||
                    request.getCurrentPassword() == null ||
                    request.getNewPassword() == null) {

                return ResponseEntity
                        .badRequest()
                        .body("All password fields are required.");
            }


            User user =
                    userService.findByEmail(
                            request.getEmail()
                    );


            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }


            boolean currentPasswordCorrect =
                    userService.checkPassword(
                            request.getCurrentPassword(),
                            user.getPassword()
                    );


            if (!currentPasswordCorrect) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Current password is incorrect.");
            }


            if (request.getNewPassword()
                    .trim()
                    .length() < 6) {

                return ResponseEntity
                        .badRequest()
                        .body("New password must be at least 6 characters.");
            }


            userService.changePassword(
                    user,
                    request.getNewPassword()
            );


            return ResponseEntity.ok(
                    "Password changed successfully."
            );


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to change password.");
        }
    }



    // =========================================
    // LOGOUT
    // =========================================

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        /*
         * Authentication is handled through
         * localStorage in this MVP.
         *
         * Therefore there is no server-side
         * session to destroy here.
         */

        return ResponseEntity.ok(
                "Logged out successfully."
        );
    }



    // =========================================
    // LOGIN REQUEST
    // =========================================

    public static class LoginRequest {

        private String email;

        private String password;


        public String getEmail() {

            return email;
        }


        public void setEmail(String email) {

            this.email = email;
        }


        public String getPassword() {

            return password;
        }


        public void setPassword(String password) {

            this.password = password;
        }
    }



    // =========================================
    // PASSWORD CHANGE REQUEST
    // =========================================

    public static class PasswordChangeRequest {

        private String email;

        private String currentPassword;

        private String newPassword;


        public String getEmail() {

            return email;
        }


        public void setEmail(String email) {

            this.email = email;
        }


        public String getCurrentPassword() {

            return currentPassword;
        }


        public void setCurrentPassword(
                String currentPassword) {

            this.currentPassword =
                    currentPassword;
        }


        public String getNewPassword() {

            return newPassword;
        }


        public void setNewPassword(
                String newPassword) {

            this.newPassword =
                    newPassword;
        }
    }

}