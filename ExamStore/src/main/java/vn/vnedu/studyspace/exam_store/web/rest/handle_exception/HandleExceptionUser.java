package vn.vnedu.studyspace.exam_store.web.rest.handle_exception;

import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

import java.util.Optional;

public class HandleExceptionUser {

    public static String getCurrentUserLogin(Optional<String> userLoginOptional, String entityName) {
        if (userLoginOptional.isEmpty()){
            throw new BadRequestAlertException("User not logged in", entityName, "userNotLoggedIn");
        }
        return userLoginOptional.get();
    }
}
