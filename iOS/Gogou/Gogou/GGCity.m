//
//  GGCity.m
//  Gogou
//
//  Created by xijunli on 16/4/22.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "GGCity.h"
#import <RestKit/RestKit.h>
#import "GoGouConstant.h"

@implementation GGCity

/*
 originList Request -- 商品原产地查询
 */
+ (void)originListRequestWithLanguageCode:(NSString *)languageCode{
    
     NSMutableArray *originList = [NSMutableArray new];
    
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeNameArray = [NSArray arrayWithObjects:@"原产地",@"美国",@"中国",@"印度",@"俄罗斯",@"法国",@"西班牙", nil];
    for (NSString *item in fakeNameArray){
        GGCity *city = [GGCity new];
        city.name =item;
        [originList addObject:city];
    }
    
    NSArray *tmp = [NSArray arrayWithArray:originList];
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:tmp forKey:@"originList"];
    [userDefaults synchronize];

}

/*
 destinationList Request -- 商品收获地查询
 */
+ (void)destinationListRequestWithLanguageCode:(NSString *)languageCode{
    
     NSMutableArray *destinationList = [NSMutableArray new];
    
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeNameArray = [NSArray arrayWithObjects:@"收货地",@"美国",@"中国",@"印度",@"俄罗斯",@"法国",@"西班牙", nil];
    for (NSString *item in fakeNameArray){
        GGCity *city = [GGCity new];
        city.name =item;
        [destinationList addObject:city];
    }
    NSArray *tmp = [NSArray arrayWithArray:destinationList];
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:tmp forKey:@"destinationList"];
    [userDefaults synchronize];
}

/*
 tripOriginList Request -- 行程出发地查询
 */
+ (void)tripOriginListRequestWithLanguageCode:(NSString *)languageCode{
    
     NSMutableArray *tripOriginList = [NSMutableArray new];
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeNameArray = [NSArray arrayWithObjects:@"出发地",@"美国",@"中国",@"印度",@"俄罗斯",@"法国",@"西班牙", nil];
    for (NSString *item in fakeNameArray){
        GGCity *city = [GGCity new];
        city.name = item;
        [tripOriginList addObject:city];
    }
    NSArray *tmp = [NSArray arrayWithArray:tripOriginList];
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:tmp forKey:@"tripOriginList"];
    [userDefaults synchronize];
}

/*
 tripDestinationList Request -- 行程目的地查询
 */
+ (void)tripDestinationListRequestWithLanguageCode:(NSString *)languageCode{
    
     NSMutableArray *tripDestinationList = [NSMutableArray new];
    
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeNameArray = [NSArray arrayWithObjects:@"目的地",@"美国",@"中国",@"印度",@"俄罗斯",@"法国",@"西班牙", nil];
    for (NSString *item in fakeNameArray){
        GGCity *city = [GGCity new];
        city.name = item;
        [tripDestinationList addObject:city];
    }
    NSArray *tmp = [NSArray arrayWithArray:tripDestinationList];
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:tmp forKey:@"tripDestinationList"];
    [userDefaults synchronize];
}



@end
