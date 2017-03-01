//
//  OrderDescription.h
//  Gogou
//
//  Created by xijunli on 16/4/20.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "GGCategory.h"

@interface OrderDescription : NSObject

@property (nonatomic) NSNumber *Id;
@property (nonatomic) NSNumber *minPrice;
@property (nonatomic) NSNumber *maxPrice;
@property (nonatomic) NSNumber *finalPrice;
@property (nonatomic) NSString *productName;
@property (nonatomic) NSNumber *quantity;
@property (nonatomic) NSString *categoryName;
@property (nonatomic) NSString *brand;
@property (nonatomic) NSString *origin;
@property (nonatomic) NSString *name;
@property (nonatomic) NSString *title;
@property (nonatomic) NSString *descriptions;
@property (nonatomic) NSString *languageCode;

@end
