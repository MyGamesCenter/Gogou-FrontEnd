//
//  DemandFilter.h
//  Gogou
//
//  Created by xijunli on 16/4/1.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface DemandFilter : NSObject

@property(nonatomic) NSNumber *Id;
@property(nonatomic) NSNumber *currentPage;
@property(nonatomic) NSNumber *pageSize;
@property(nonatomic) NSString *userName;
@property(nonatomic) NSString *serviceFeeOrder;
@property(nonatomic) NSString *ratingOrder;
@property(nonatomic) NSString *productname;
@property(nonatomic) NSString *origin;
@property(nonatomic) NSString *destination;
@property(nonatomic) NSString *categoryName;

@end
