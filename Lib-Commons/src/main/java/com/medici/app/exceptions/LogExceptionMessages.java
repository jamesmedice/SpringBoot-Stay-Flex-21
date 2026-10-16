package com.medici.app.exceptions;

public interface LogExceptionMessages {

    public static final String RATING_MUST_BE_BETWEEN_1_AND_5 = "Rating must be between 1 and 5";

    public static final String EXTERNAL_URL_FABRICK_SET_ON_THE_API_CLIENT_0 = "External Url set on the api FABRICK client: {}";

    public static final String EXTERNAL_URL_SET_ON_THE_API_CLIENT_0 = "External Url set on the api HCP client: {}";

    public static final String EXTERNAL_URL_SET_ON_THE_API_IQP_CLIENT_0 = "External Url set on the api IQP client: {}";

    public static final String EXTERNAL_URL_SET_ON_THE_API_TOKEN_CLIENT_0 = "External Url set on the api TOKEN client: {}";

    public static final String UPDATE_STATUS_RESPONSE = "UPDATE By UUID : {}";

    public static final String CALLING_API = "CALLING API: {0}";

    public static final String USER_ALREADY_EXISTS = "User already exists: {0}, {1}";

    public static final String ENTITY_NOT_FOUND = "Entity not found: {0}";

    public static final String STATUS_NOT_RESPECTED = "Status not respected: {0}";

    public static final String DEVICE_NOT_RESPECTED = "Device not respected";

    public static final String DELETED_BY_UUID = "DELETED by UUID: {0}";

    public static final String ANSWER_DECLINED = "Answer declined for: {0}, {1}";

    public static final String CALLING_PUBSUB_TOPIC_D_WITH_MESSAGE_D = "Calling pubsub topic {0}, with message: {1}";

    public static final String ACCOUNT_SERVICE_OR_CONSENT_SERVICE_FAILED_TO_INSERT_RECORDS = "Account Service or Consent Service failed to insert records.";

    public static final String ACCOUNT_COULD_NOT_HAVE_BEEN_CREATED = "Account could not have been created {0} {1}";

    public static final String ACCOUNT_UUID_NOT_FOUND = "AccountUUID not found. {0}";

    public static final String CONSENT_NOT_FOUND_BY_ACCOUNT_UUID_AND_CONSENT_CODE = "Consent not found by AccountUUID and ConsentCode. {0} {1}";

    public static final String ACCOUNT_NOT_FOUND = "Account not found: {0}";

    public static final String USER_IS_ALREADY_ASSOCIATED_WITH_THIS_ACCOUNT = "User is already associated with this account";

    public static final String INSUFFICIENT_AVAILABLE_FUNDS = "Insufficient available funds";

    public static final String CARDHOLDER_IS_NOT_AN_ACTIVE_MEMBER_OF_THIS_ACCOUNT = "Cardholder is not an active member of this account";

    public static final String IDEMPOTENCY_KEY_WAS_ALREADY_USED_FOR_A_DIFFERENT_REQUEST = "Idempotency key was already used for a different request";

    public static final String DEPOSIT_AMOUNT_MUST_BE_POSITIVE = "Deposit amount must be positive";

    public static final String TIMED_OUT_WAITING_TO_START = "Timed out waiting to start";

    public static final String ACCOUNT_DOES_NOT_EXISTS = "Account does not exists. {0}";

    public static final String ACCOUNT_COULDN_T_HAVE_BEEN_UPDATED = "Account cound not have been updated. {0}";

    public static final String DEVICE_COULDN_T_HAVE_BEEN_CREATED = "Device cound not have been created.";

    public static final String ACCOUNT_S_DEVICES_DO_NOT_EXIST = "Account-Devices do not exist. {0}";

    public static final String DEVICE_DOES_NOT_EXISTS = "Device does not exists. {0}";

    public static final String ACCOUNT_COULDN_T_HAVE_BEEN_CREATED = "Account cound not have been created.";

    public static final String ACCOUNT_UUID = "account-uuid";

    public static final String FILE_DELETED = "File {0} has been deleted {1}";

    public static final String FILE_DELETED_FROM_BUCKET = "File {0} has been deleted from bucket {1}.  {2}";

    public static final String STUDENT_API_STUDENT_STATUS_UPDATE = "studentApi.studentStatusUpdate: {0}, {1}, {2}";

    public static final String STUDENT_API_STUDENT_PATCH_FOR_APPLICATION = "studentApi.studentPatchForApplication: {0}, {1}, {2}, {3}";

    public static final String DELETED_ACCOUNT_AND_DEVICE = "DELETED ACCOUNT AND DEVICE";

    public static final String DELETED_USER_EVENT = "DELETED USER EVENT";

    public static final String DELETED_CONSENT = "DELETED CONSENT";

    public static final String STUDENT_API_STUDENT_DELETE = "Student delete: {0}";

    public static final String CONSENT_BY_CODE_NOT_FOUND = "Consent by code not found: {0}";

    public static final String CONFIG_PROPERTIES_BY_KEY_NOT_FOUND = "Config properties by key not found: {0}";

    public static final String OCR_ERROR_RESPONSE = "Code: {0}, Message: {1}, Detail: {2}";

    public static final String TOKEN_BEARER = "Bearer %s";

    public static final String CALLING_INFOCERT_EXECUTION = "CALLING INFOCERT WITH REQUEST: {0}";

    public static final String OCR_STATUS_0_OVERALL_SCORE_1_SEMAPHORE_VALUE_2_LAST_OCR_RESULT_3_MAX_RETRY_4 = "OcrStatus: {0}, OverallScore: {1}, SemaphoreValue: {2}, LastOcrResult: {3}, MaxRetry: {4}";

    public static final String OCR_STATUS_0_OVERALL_SCORE_1_MAX_RETRY_2 = "OcrStatus: {0}, OverallScore: {1}, MaxRetry: {2}";

    public static final String OCR_STATUS_0_OVERALL_SCORE_1_SEMAPHORE_VALUE_2_LAST_OCR_RESULT_3 = "OcrStatus: {0}, OverallScore: {1}, SemaphoreValue: {2}, LastOcrResult: {3}";

    public static final String CALL_WORKFLOWSERVICE_START = "WORKFLOWSERVICE.START: {0}, {1}";

    public static final String WORKFLOWSERVICE_PROCESSEVENT = "WORKFLOWSERVICE.PROCESSEVENT: {0}, {1}, {2}";

    public static final String PROCESSAPI_STARTPROCESSUSINGPOST = "PROCESSAPI.STARTPROCESSUSINGPOST: {0}";

    public static final String PROCESSAPI_PERFORMACTIONUSINGPUT = "PROCESSAPI.PERFORMACTIONUSINGPUT: {0}";

    public static final String RECOGNITIONAPI_SAVERECOGNITION = "RECOGNITIONAPI.SAVERECOGNITION: {0}";

    public static final String WORKFLOWSERVICE_CANCEL = "WORKFLOWSERVICE.CANCEL: {0}";

    public static final String DEFAULT_SERVICE_RESPONSE = "RESPONSE: {0}";

    public static final String RUN_IDLE_CONNECTION_MONITOR_CLOSING_EXPIRED_AND_IDLE_CONNECTIONS = "run IdleConnectionMonitor - Closing expired and idle connections...";

    public static final String RUN_IDLE_CONNECTION_MONITOR_HTTP_CLIENT_CONNECTION_MANAGER_IS_NOT_INITIALISED = "run IdleConnectionMonitor - Http Client Connection manager is not initialised";

    public static final String RUN_IDLE_CONNECTION_MONITOR_EXCEPTION_OCCURRED_MSG_0_E_1 = "run IdleConnectionMonitor - Exception occurred. msg={0}, e={1}";

    public static final String RECOGNITIONAPI_PUTRECOGNITIONSTATUS = "RECOGNITIONAPI.PUTRECOGNITIONSTATUS: {0}, {1}, {2}";

    public static final String APPLICATIONAPI_APPLICATIONPROGRESS = "APPLICATIONAPI.APPLICATIONPROGRESS: {0}, {1}, {2}";

    public static final String OAUTH_TOKEN = "/oauth/token";

    public static final String URL_0_ACCOUNT_ID_1_FROM_DATE_2_FUTURE_3_LIMIT_4_OFFSET_5_SORT_BY_6_TO_DATE_7_TYPE_8 = "URL {0}, accountId {1}, fromDate {2}, future {3}, limit {4}, offset {5}, sortBy {6}, toDate {7}, type {8}";

    public static final String ACCOUNT_ID_IS_REQUIRED = "Account ID is required";

    public static final String BALANCE_MUST_BE_NON_NEGATIVE = "Balance must be non-negative";

    public static final String AMOUNT_MUST_BE_POSITIVE = "Amount must be positive";

    public static final String IDEMPOTENCY_KEY_ALREADY_USED_WITH_DIFFERENT_REQUEST_DATA = "Idempotency key already used with different request data";

    public static final String CONCURRENT_ACCOUNT_UPDATE_RETRY_WITH_THE_SAME_IDEMPOTENCY_KEY = "Concurrent account update; retry with the same idempotency key";

}
