//
//  Requirement.h
//  WorldShopping
//
//  Created by xijunli on 15/11/18.
//  Copyright © 2015年 laimeng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Requirement : NSObject{
    //声明成员变量
    NSNumber *Id;
    NSNumber *accountId;
    NSString *itemTypeName;
    NSString *itemTypeIcon;
    NSString *username;
    NSString *originName;
    NSString *originCountry;
    NSString *receipePlaceName;
    NSString *receipePlaceCountry;
    NSString *bankName;
    NSNumber *receiptTime;
    NSNumber *num;
    NSNumber *price;
    NSString *detail;
    NSString *photos;
}

@property(nonatomic,strong) NSNumber *Id;
@property(nonatomic,strong) NSNumber *accountId;
@property(nonatomic,strong) NSString *itemTypeName;
@property(nonatomic,strong) NSString *itemTypeIcon;
@property(nonatomic,strong) NSString *username;
@property(nonatomic,strong) NSString *originName;
@property(nonatomic,strong) NSString *originCountry;
@property(nonatomic,strong) NSString *receipePlaceName;
@property(nonatomic,strong) NSString *receipePlaceCountry;
@property(nonatomic,strong) NSString *bankName;
@property(nonatomic,strong) NSNumber *receiptTime;
@property(nonatomic,strong) NSNumber *num;
@property(nonatomic,strong) NSNumber *price;
@property(nonatomic,strong) NSString *detail;
@property(nonatomic,strong) NSString *photos;

+(Requirement*)getRequirementWithDictionary:(NSDictionary*)dict;

-(Requirement*)initWithDictionary:(NSDictionary*)dict;

-(Requirement*)initForRequListWithType:(NSString*)itemTypeName
                            originName:(NSString*)originName
                                detail:(NSNumber*)detail
                                requID:(NSNumber*)requID;
@end
