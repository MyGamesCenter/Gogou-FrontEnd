//
//  Requirement.m
//  WorldShopping
//
//  Created by xijunli on 15/11/18.
//  Copyright © 2015年 laimeng. All rights reserved.
//

#import "Requirement.h"

@implementation Requirement

@synthesize Id;
@synthesize accountId;
@synthesize itemTypeName;
@synthesize username;
@synthesize itemTypeIcon;
@synthesize originName;
@synthesize originCountry;
@synthesize receipePlaceName;
@synthesize receipePlaceCountry;
@synthesize bankName;
@synthesize receiptTime;
@synthesize num;
@synthesize price;
@synthesize detail;
@synthesize photos;

+(Requirement*)getRequirement:(NSDictionary *)dict{
    Requirement* requirement = [Requirement new];
    requirement.Id = [dict objectForKey:@"id"];
    requirement.accountId = [dict objectForKey:@"accountId"];
    requirement.itemTypeName = [dict objectForKey:@"itemTypeName"];
    requirement.itemTypeIcon = [dict objectForKey:@"itemTypeIcon"];
    requirement.username = [dict objectForKey:@"username"];
    requirement.originName = [dict objectForKey:@"origin"];
    requirement.receipePlaceName = [dict objectForKey:@"receipePlaceName"];
    requirement.bankName = [dict objectForKey:@"bankName"];
    requirement.receiptTime = [dict objectForKey:@"receiptTime"];
    requirement.num = [dict objectForKey:@"num"];
    requirement.price = [dict objectForKey:@"price"];
    requirement.photos = [dict objectForKey:@"photos"];
    requirement.detail = [dict objectForKey:@"detail"];
    
    return requirement;
}

+ (Requirement*) getRequirementWithDictionary:(NSDictionary *)dict{
    Requirement* requirement = [[Requirement alloc]initWithDictionary:dict];
    return requirement;
}

- (Requirement*) initWithDictionary:(NSDictionary *)dict{
    self = [super init];
    if (self)
    {
        self.Id = [dict objectForKey:@"id"];
        self.accountId = [dict objectForKey:@"accountId"];
        self.itemTypeName = [dict objectForKey:@"itemTypeName"];
        self.itemTypeIcon = [dict objectForKey:@"itemTypeIcon"];
        self.username = [dict objectForKey:@"username"];
        self.originName = [dict objectForKey:@"origin"];
        self.receipePlaceName = [dict objectForKey:@"receipePlaceName"];
        self.bankName = [dict objectForKey:@"bankName"];
        self.receiptTime = [dict objectForKey:@"receiptTime"];
        self.num = [dict objectForKey:@"num"];
        self.price = [dict objectForKey:@"price"];
        self.photos = [dict objectForKey:@"photos"];
        self.detail = [dict objectForKey:@"detail"];
    }
    return self;
}

-(Requirement*)initForRequListWithType:(NSString*)itemtypeName
                            originName:(NSString*)originname
                                detail:(NSString*)detail1
                                requID:(NSNumber*)requID{
    self = [super init];
    if (self)
    {
        self.itemTypeName = itemtypeName;
        self.originName = originname;
        self.detail = detail1;
        self.Id = requID;
    }
    return self;
}
@end
