package org.vinevweb.cardiohristov.common;

public final class Constants {


    //BINDING MODELS CONSTANTS-------------------------------------------------------------------------------------------

    public static final String APPOINTMENT_NAME_COULD_NOT_BE_NULL =  "Полето за име и фамилия не може да е с нулева стойност.";
    public static final String APPOINTMENT_NAME_COULD_NOT_BE_EMPTY =  "Полето за име и фамилия не може да е празно.";
    public static final String APPOINTMENT_NAME_PATTERN =  "^[A-Z][a-zA-Z]+ [A-Z][a-zA-Z]+$";
    public static final String APPOINTMENT_NAME_REQUIREMENTS  = "Името и фамилията трябва да започват с главна буква, да са изписани с латински букви и да са разделени с интервал";
    public static final String APPOINTMENT_NUMBER_COULD_NOT_BE_NULL  = "Телефонният номер не може да е с нулева стойност.";
    public static final String APPOINTMENT_NUMBER_COULD_NOT_BE_EMPTY =  "Полето за телефонен номер не може да е празно.";
    public static final String APPOINTMENT_NUMBER_PATTERN =   "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
    public static final String APPOINTMENT_NUMBER_INVALID_MSG =   "Невалиден телефонен номер.";
    public static final String APPOINTMENT_DATE_COULD_NOT_BE_NULL =  "Полето за дата не може да е с нулева стойност.";
    public static final String APPOINTMENT_DATE_COULD_NOT_BE_EMPTY =  "Полето за дата не може да е празно.";
    public static final String APPOINTMENT_TIME_COULD_NOT_BE_NULL =  "Полето за време не може да е с нулева стойност.";
    public static final String APPOINTMENT_TIME_COULD_NOT_BE_EMPTY =  "Полето за време не може да е празно.";
    public static final String APPOINTMENT_DATETIME_FORMAT =  "yyyy-MM-dd HH:mm";

    public static final String PROCEDURE_DATETIME_FORMAT =  "yyyy-MM-dd'T'HH:mm";

    public static final String USER_EMAIL_COULD_NOT_BE_NULL = "Имейлът не може да е с нулева стойност.";
    public static final String USER_EMAIL_COULD_NOT_BE_EMPTY = "Полето за имайл не може да е празно.";
    public static final String USER_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String INVALID_EMAIL = "Невалиден имайл.";
    public static final String USER_PASSWORD_COULD_NOT_BE_NULL = "Паролата не може да е с нулева стойност.";
    public static final String USER_PASSWORD_COULD_NOT_BE_EMPTY = "Полето за паролата не може да е празно.";
    public static final String USER_PASSWORD_REQUIREMENTS = "Паролата трябва да е с дължина между 4 и 20 символа.";
    public static final String USER_NAME_COULD_NOT_BE_NULL = "Името не може да е с нулева стойност.";
    public static final String USER_NAME_COULD_NOT_BE_EMPTY = "Полето за името не може да е празно.";
    public static final String USER_NAME_LENGTH_REQUIREMENTS = "Името трябва да съдържа минимум 2 букви.";
    public static final String USER_NAME_PATTERN = "^[A-Z][a-zA-Z]+";
    public static final String USER_NAME_REQUIREMENTS = "Името трябва да започва с главна буква, и да е изписано с латински букви.";
    public static final String USER_FAMILYNAME_COULD_NOT_BE_NULL = "Фамилията не може да е с нулева стойност.";
    public static final String USER_FAMILYNAME_COULD_NOT_BE_EMPTY = "Полето за фамилия не може да е празно.";
    public static final String USER_FAMILYNAME_LENGTH_REQUIREMENTS = "Фамилията трябва да съдържа минимум 2 букви.";
    public static final String USER_FAMILYNAME_PATTERN = "^[A-Z][a-zA-Z]+";
    public static final String USER_FAMILYNAME_REQUIREMENTS = "Фамилията трябва да започва с главна буква, и да е изписана с латински букви.";

    //SERVICES CONSTANTS-------------------------------------------------------------------------------------------

        //USER
    public static final String WRONG_NON_EXISTENT_ID = "Wrong or non-existent id.";
    public static final String WRONG_NON_EXISTENT_EMAIL = "Wrong or nonexistent email.";
    public static final String USER_ALREADY_EXISTS_ERROR = "Registering user %s failed. User with that email already exists!";
    public static final String USER_HAS_BEEN_EDITED = "Редактиран е потребител с имейл: ";
    public static final String ROLE_HAS_BEEN_CHANGED = "Променена е ролята на потребител с имейл: ";
    public static final String USER_HAS_BEEN_DELETED = "Изтрит е потребител с имейл: ";
    public static final String ROLE_ROOT = "ROLE_ROOT";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String THE_ROLE_DOES_NOT_EXISTS = "The role does not exists.";
    public static final String ADMIN = "admin";
    public static final String MODERATOR = "moderator";
    public static final String USER = "user";

        //APPOINTMENT
    public static final String START_TIME = "00:01";
    public static final String END_TIME = "23:59";
    public static final String APPOINTMENT_HAS_BEEN_DELETED = "Изтрит е запазен час на името на %s за %s: ";
    public static final String APPOINTMENT_HAS_BEEN_EDITED = "Променен е запазен час на името на %s за %s: ";
    public static final String CRON_VALUE = "0 0 1 * * SUN";

        //ARTICLE
    public static final String ARTICLE_HAS_BEEN_CREATED = "Създадена е статия със заглавие: ";
    public static final String ARTICLE_HAS_BEEN_DELETED = "Изтрита е статия със заглавие: ";

        //LOGGER
    public static final String LOGGER_FULL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

        //PROCEDURE
    public static final String PROCEDURE_HAS_BEEN_CREATED = "Създадена е услуга със заглавие: ";
    public static final String PROCEDURE_HAS_BEEN_DELETED = "Изтрита е услуга със заглавие: ";

        //TESTIMONIAL
    public static final String TESTIMONIAL_HAS_BEEN_DELETED = "Изтрит е отзив с текст: ";

    //CONTROLLERS-------------------------------------------------------------------------------------------

        //ADMIN
    public static final String PASSWORDS_DID_NOT_MATCH = "Паролите не съвпадат!";
    public static final String TITLE_USERS = "Потребители";
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String TITLE_LOGS = "Логове";
        //APPOINTMENT
    public static final String APPOINTMENT_CREATE_FAILURE_EXCEPTION = "Invalid data for creating appointment!";
    public static final String TITLE_SCHEDULE = "График";
        //ARTICLE
        public static final String TITLE_ARTICLES = "Статии";
    public static final String TITLE_ARTICLES_CREATE = "Статии - Създаване";
    public static final String TITLE_SINGLE_ARTICLE = "Полезна статия";
        //HOME
        public static final String TITLE_CONTACTS = "Контакти";
        //PROCEDURE
        public static final String PROCEDURE_PICTURE_UPLOAD_FAILED = "Procedure Picture upload failed.";
    public static final String PROCEDURE_CREATION_FAILED = "Procedure creation failed.";
    public static final String TITLE_PROCEDURES = "Услуги";
    public static final String TITLE_CREATE_PROCEDURE = "Създаване на услуга";
    public static final String TITLE_SINGLE_PROCEDURE = "Описание на услугата";
        //TESTIMONIALS
        public static final String TITLE_TESTIMONIALS = "Отзиви";
        //USER
        public static final String USER_REGISTRATION_FAILURE = "Registering user %s failed.";

    //EXCEPTIONS
   public static final String COMMENT_CREATION_ERROR = "Error occurred during comment creation.";
   public static final String APPOINTMENT_CREATION_ERROR = "Error occurred during appointment creation.";
   public static final String ARTICLE_CREATION_ERROR = "Error occurred during article creation.";
   public static final String NON_EXISTENT_ID = "Non-existent id.";
   public static final String NON_EXISTENT_NAME = "Non-existent name.";
   public static final String PROCEDURE_CREATION_ERROR = "Error occurred during procedure creation.";
   public static final String TESTIMONIAL_CREATION_ERROR = "Error occurred during testimonial creation.";
   public static final String DUPLICATE_USER_NAME_ERROR = "User with the given name already exists.";
   public static final String USER_PROFILE_EDIT_ERROR = "Error occurred during user profile edit.";
   public static final String USER_REGISTRATION_ERROR = "Error occurred during user registration.";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";


    private Constants(){}
}
