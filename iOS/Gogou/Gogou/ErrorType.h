//
//  ErrorType.h
//  Gogou
//
//  Created by letian xu on 2016-07-04.
//  Copyright © 2016 GoGou Inc. All rights reserved.
//

typedef enum {
    NONE = 0,
    ERR_EMAIL_FORMAT,
    EER_TIME_EXPIRED,
    EER_USER_EXISTS,
    EER_NO_USER_EXISTS,
    EER_NO_REG_CODE,
    EER_REG_CODE_SENT,
    EER_NOT_MATCH,
    EER_ACCESS_TOKEN_INVALID,
    EER_ORDER_NOT_PROCESSED,
    EER_UNKNOWN
} ErrorType;
