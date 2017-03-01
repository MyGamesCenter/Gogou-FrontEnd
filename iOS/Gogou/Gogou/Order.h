//
//  Order.h
//  Gogou
//
//  Created by xijunli on 16/4/16.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "OrderDescription.h"
#import "OrderStatus.h"
#import "PaymentType.h"
#import "Address.h"


@interface Order : NSObject

@property (nonatomic) NSString *id;
@property (nonatomic) NSNumber *tripId;
@property (nonatomic) NSString *username;
@property (nonatomic) OrderStatus orderStatus;
@property (nonatomic) NSNumber *serviceFee;
@property (nonatomic) NSMutableArray *orderDescriptions;
@property (nonatomic) NSNumber *currencyId;
@property (nonatomic) NSNumber *currencyValue;
//@property (nonatomic) Locale *locale;
@property (nonatomic) NSString *orderType;
@property (nonatomic) NSNumber *orderTotal;
@property (nonatomic) PaymentType paymentType;
@property (nonatomic) NSString *sellerId;
@property (nonatomic) Address *address;


@end
