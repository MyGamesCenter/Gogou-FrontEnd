//
//  GGTime.m
//  Gogou
//
//  Created by xijunli on 16/4/22.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "GGTime.h"
#import <RestKit/RestKit.h>
#import "GoGouConstant.h"

@implementation GGTime

/*
 arrivalTimeList Request -- 到达时间查询
 */
+ (void)arrivalTimeListRequestWithLanguageCode:(NSString *)languageCode{
    
    
    NSMutableArray *arrivalTimeList = [NSMutableArray new];;
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
    NSArray *fakeTimeArray = [NSArray arrayWithObjects:@"到达时间",@"今天",@"明天", nil];
    for (NSString *item in fakeTimeArray){
        GGTime *time = [GGTime new];
        time.name = item;
        [arrivalTimeList addObject:time];
    }
    
    NSArray *tmp = [NSArray arrayWithArray:arrivalTimeList];
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:tmp forKey:@"arrivalTimeList"];
    [userDefaults synchronize];
}


@end
