package thinh.springboot.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalException {
    /**
    * Handle exception when validate data
    *
    * @param e
    * @param request
    * @return errorResponse
    */
   @ExceptionHandler({ConstraintViolationException.class, // Error in request body
                   MissingServletRequestParameterException.class, // Error in request params
                   MethodArgumentNotValidException.class})
   @ResponseStatus(BAD_REQUEST) // ~ 400
   public ErrorResponse handleValidationException(Exception e, WebRequest request) {
       ErrorResponse errorResponse = new ErrorResponse();
       errorResponse.setTimestamp(new Date());
       errorResponse.setStatus(BAD_REQUEST.value());
       errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

       String message = e.getMessage();
       if (e instanceof MethodArgumentNotValidException) {
           int start = message.lastIndexOf("[") + 1;
           int end = message.lastIndexOf("]") - 1;
           message = message.substring(start, end);
           errorResponse.setError("Invalid Payload");
           errorResponse.setMessage(message);
       } else if (e instanceof MissingServletRequestParameterException) {
           errorResponse.setError("Invalid Parameter");
           errorResponse.setMessage(message);
       } else if (e instanceof ConstraintViolationException) {
           errorResponse.setError("Invalid Parameter");
           errorResponse.setMessage(message.substring(message.indexOf(" ") + 1));
       } else {
           errorResponse.setError("Invalid Data");
           errorResponse.setMessage(message);
       }

       return errorResponse;
   }

   /**
    * Handle exception when the request not found data
    *
    * @param e
    * @param request
    * @return
    */
   @ExceptionHandler(ResourceNotFoundException.class)
   @ResponseStatus(NOT_FOUND) // ~ 404
   public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
       ErrorResponse errorResponse = new ErrorResponse();
       errorResponse.setTimestamp(new Date());
       errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
       errorResponse.setStatus(NOT_FOUND.value());
       errorResponse.setError(NOT_FOUND.getReasonPhrase());
       errorResponse.setMessage(e.getMessage());

       return errorResponse;
   }

   /**
    * Handle exception when the data is conflicted
    *
    * @param e
    * @param request
    * @return
    */
   @ExceptionHandler(InvalidDataException.class)
   @ResponseStatus(CONFLICT) // ~ 409
   public ErrorResponse handleDuplicateKeyException(InvalidDataException e, WebRequest request) {
       ErrorResponse errorResponse = new ErrorResponse();
       errorResponse.setTimestamp(new Date());
       errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
       errorResponse.setStatus(CONFLICT.value());
       errorResponse.setError(CONFLICT.getReasonPhrase());
       errorResponse.setMessage(e.getMessage());

       return errorResponse;
   }

   /**
    * Handle exception when internal server error
    *
    * @param e
    * @param request
    * @return error
    */
   @ExceptionHandler(Exception.class)
   @ResponseStatus(INTERNAL_SERVER_ERROR) // ~ 500
   public ErrorResponse handleException(Exception e, WebRequest request) {
       ErrorResponse errorResponse = new ErrorResponse();
       errorResponse.setTimestamp(new Date());
       errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
       errorResponse.setStatus(INTERNAL_SERVER_ERROR.value());
       errorResponse.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
       errorResponse.setMessage(e.getMessage());

       return errorResponse;
   }

    /**
     * Handle exception when the request not found data
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse handleForBiddenException(AccessDeniedException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(FORBIDDEN.value());
        errorResponse.setError(FORBIDDEN.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }

   @Getter
   @Setter
   private class ErrorResponse {
       private Date timestamp;
       private int status;
       private String path;
       private String error;
       private String message;
   }
}
