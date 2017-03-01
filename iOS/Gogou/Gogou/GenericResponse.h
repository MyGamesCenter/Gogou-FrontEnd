//
//  GenericResponse.h
//  Gogou
//
//  Created by xijunli on 16/3/12.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ErrorType.h"

@interface GenericResponse : NSObject

@property (nonatomic) NSString *message;
@property (nonatomic) NSString *error; //to be removed
@property (nonatomic) ErrorType errorType;

@end
