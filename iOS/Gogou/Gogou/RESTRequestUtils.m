//
//  RESTRequestUtils.m
//  Gogou
//
//  Created by letian xu on 2016-07-04.
//  Copyright © 2016 GoGou Inc. All rights reserved.
//

#import "RESTRequestUtils.h"
#import "GoGouConstant.h"
#import "GenericResponse.h"
#import "PaymentType.h"
#import "OrderStatus.h"
#import "Address.h"
#import "OrderDescription.h"

#import <RestKit/RestKit.h>

@implementation RESTRequestUtils

+ (void)performGetRegCodeRequest:(NSString *)username
                    emailAddress:(NSString *)emailAddress
                        delegate:(id<RESTRequestListener>)delegate
{
    NSString *const REST_END = @"/gogou-web/services/subscriber/getRegCode?username=%@&emailAddress=%@";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",
                                                     @"errorType"]];

    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodGET
                                                                                       pathPattern:nil
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    NSString *pseudoRequest = [NSString stringWithFormat:@"%@%@", REST_SERVICES_URL, REST_END];
    NSString *requestString = [NSString stringWithFormat:pseudoRequest, username, emailAddress];
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request
                                                                        responseDescriptors:@[responseDescriptor]];
    
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result)
    {
        GenericResponse *genericResponse = [result firstObject];
        NSLog(@"Get registration code: %@", [genericResponse message]);
        [delegate onGogouRESTRequestSuccess:genericResponse];
    }
                                     failure:^(RKObjectRequestOperation *operation, NSError *error)
    {
        NSLog(@"Failed with error: %@", [error localizedDescription]);
        [delegate onGogouRESTRequestFailure];
    }];
    [operation start];
}

+ (void)performOAuth2LoginWith:(NSString *)code
                      delegate:(id<RESTRequestListener>)delegate{
    NSString *const REST_END = @"/gogou-web/services/Oauth2Login/getAccessToken?code=%@";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[OAuth2Login class]];
    [responseMapping addAttributeMappingsFromArray:@[@"errcode",
                                                     @"errmsg",
                                                     @"accessToken",
                                                     @"refreshToken",
                                                     @"nickname",
                                                     @"gender",
                                                     @"headimgurl",
                                                     @"fileOutput"]];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodGET
                                                                                       pathPattern:nil
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    NSString *pseudoRequest = [NSString stringWithFormat:@"%@%@", REST_SERVICES_URL, REST_END];
    NSString *requestString = [NSString stringWithFormat:pseudoRequest, code];
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request
                                                                        responseDescriptors:@[responseDescriptor]];
    
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result)
     {
         OAuth2Login *oauth2login = [result firstObject];
         
         [delegate onGogouRESTRequestSuccess:oauth2login];
     }
                                     failure:^(RKObjectRequestOperation *operation, NSError *error)
     {
         NSLog(@"Failed with error: %@", [error localizedDescription]);
         [delegate onGogouRESTRequestFailure];
     }];
    [operation start];

}

+ (void)performCheckAccessTokenWith:(NSString *)savedToken
                           delegate:(id<RESTRequestListener>)delegate{
    NSString *const REST_END = @"/gogou-web/services/Oauth2Login/checkAccessToken?access_token=%@";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",
                                                     @"errorType"]];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodGET
                                                                                       pathPattern:nil
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    NSString *pseudoRequest = [NSString stringWithFormat:@"%@%@", REST_SERVICES_URL, REST_END];
    NSString *requestString = [NSString stringWithFormat:pseudoRequest, savedToken];
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request
                                                                        responseDescriptors:@[responseDescriptor]];
    
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result)
     {
         GenericResponse *genericResponse = [result firstObject];
         NSLog(@"Get access token: %@", [genericResponse message]);
         [delegate onGogouRESTRequestSuccess:genericResponse];
     }
                                     failure:^(RKObjectRequestOperation *operation, NSError *error)
     {
         NSLog(@"Failed with error: %@", [error localizedDescription]);
         [delegate onGogouRESTRequestFailure];
     }];
    [operation start];
    
}

+ (void)performRefreshAccessTokenWith:(NSString *)refreshToken
                           delegate:(id<RESTRequestListener>)delegate{
    NSString *const REST_END = @"/gogou-web/services/Oauth2Login/refreshAccessToken?refresh_token=%@";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[OAuth2Login class]];
    [responseMapping addAttributeMappingsFromArray:@[@"errmsg",
                                                     @"accessToken",
                                                     @"refreshToken",
                                                     @"nickname",
                                                     @"gender",
                                                     @"headimgurl",
                                                     @"fileOutput"]];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodGET
                                                                                       pathPattern:nil
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    NSString *pseudoRequest = [NSString stringWithFormat:@"%@%@", REST_SERVICES_URL, REST_END];
    NSString *requestString = [NSString stringWithFormat:pseudoRequest, refreshToken];
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request
                                                                        responseDescriptors:@[responseDescriptor]];
    
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result)
     {
         OAuth2Login *oauth2login = [result firstObject];
         
         [delegate onGogouRESTRequestSuccess:oauth2login];
         
     }
                                     failure:^(RKObjectRequestOperation *operation, NSError *error)
     {
         NSLog(@"Failed with error: %@", [error localizedDescription]);
         [delegate onGogouRESTRequestFailure];
     }];
    [operation start];
    
}


+ (void)performCreateSubscriberRequest:(Subscriber *)subscriber
                              delegate:(id<RESTRequestListener>)delegate
{
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
    
    NSString *const REST_END = @"/gogou-web/services/subscriber/registration";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",
                                                     @"errorType"]];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodPOST
                                                                                       pathPattern:REST_END
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    [requestMapping addAttributeMappingsFromDictionary:@{@"emailAddress" : @"emailAddress",
                                                         @"encodedPassword" : @"encodedPassword",
                                                         @"clearPassword" : @"clearPassword",
                                                         @"userName" : @"userName",
                                                         @"isPurchaser" : @"isPurchaser",
                                                         @"age" : @"age",
                                                         @"firstName" : @"firstName",
                                                         @"lastName" : @"lastName",
                                                         @"gender" : @"gender",
                                                         @"gcmToken" : @"gcmToken",
                                                         @"imToken" : @"imToken",
                                                         @"regCode" : @"regCode"}];
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping
                                                                                   objectClass:[Subscriber class]
                                                                                   rootKeyPath:nil
                                                                                        method:RKRequestMethodPOST];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    
    NSData *jsonPayload = [self getJSONpayloadFromObject:subscriber
                                       requestDescriptor:requestDescriptor];
    
    NSMutableURLRequest *request = [manager multipartFormRequestWithObject:nil
                                                                    method:RKRequestMethodPOST
                                                                      path:REST_END
                                                                parameters:nil
                                                 constructingBodyWithBlock:^(id<AFMultipartFormData> formData)
    {
        [formData appendPartWithFileData:jsonPayload
                                    name:@"subscriber"
                                fileName:@"dummy"
                                mimeType:RKMIMETypeJSON];
    }];
    
    RKObjectRequestOperation *operation =
    [manager objectRequestOperationWithRequest:request
                                       success:^(RKObjectRequestOperation *operation,RKMappingResult *mappingResult)
    {
        GenericResponse *genericResponse = [mappingResult firstObject];
        NSLog(@"Create subscriber: %@", [genericResponse message]);
        [delegate onGogouRESTRequestSuccess:genericResponse];
    }

                                       failure:^(RKObjectRequestOperation *operation, NSError *error)
    {
        NSLog(@"Failed with error: %@", [error localizedDescription]);
        [delegate onGogouRESTRequestFailure];
    }];
    
    [manager enqueueObjectRequestOperation:operation];
}

+ (void)performUpdateSubsriberRequest:(Subscriber *)subscriber
                             delegate:(id<RESTRequestListener>)delegate
{
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
    
    NSString *const REST_END = @"/gogou-web/services/subscriber/update";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",
                                                     @"errorType"]];
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodPOST
                                                                                       pathPattern:REST_END
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    [requestMapping addAttributeMappingsFromDictionary:@{@"emailAddress" : @"emailAddress",
                                                         @"encodedPassword" : @"encodedPassword",
                                                         @"clearPassword" : @"clearPassword",
                                                         @"userName" : @"userName",
                                                         @"isPurchaser" : @"isPurchaser",
                                                         @"age" : @"age",
                                                         @"gender" : @"gender",
                                                         @"gcmToken" : @"gcmToken",
                                                         @"imCode" : @"imCode",
                                                         @"imToken" : @"imToken",
                                                         @"firstName" : @"firstName",
                                                         @"lastName" : @"lastName"}];
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping
                                                                                   objectClass:[Subscriber class]
                                                                                   rootKeyPath:nil
                                                                                        method:RKRequestMethodPOST];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    //添加用户信息的http headers
    NSString *authorization = [NSString stringWithFormat:@"%@:%@", subscriber.userName, subscriber.encodedPassword];
    NSString *base64authorization = [[authorization dataUsingEncoding:NSUTF8StringEncoding] base64EncodedStringWithOptions:0];
    NSString *authorizationString = @"Basic ";
    authorizationString = [authorizationString stringByAppendingString:base64authorization];
    [manager.HTTPClient setDefaultHeader:@"Authorization" value:authorizationString];

    NSData *jsonPayload = [self getJSONpayloadFromObject:subscriber
                                       requestDescriptor:requestDescriptor];
    
    NSMutableURLRequest *request = [manager multipartFormRequestWithObject:nil
                                                                    method:RKRequestMethodPOST
                                                                      path:REST_END
                                                                parameters:nil
                                                 constructingBodyWithBlock:^(id<AFMultipartFormData> formData)
    {
        [formData appendPartWithFileData:jsonPayload
                                    name:@"subscriber"
                                fileName:@"dummy"
                                mimeType:RKMIMETypeJSON];
    }];
    
    RKObjectRequestOperation *operation =
    [manager objectRequestOperationWithRequest:request
                                       success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult)
     {
         GenericResponse *genericResponse = [mappingResult firstObject];
         NSLog(@"Update subscriber: %@", [genericResponse message]);
         [delegate onGogouRESTRequestSuccess:genericResponse];
     }
                                       failure:^(RKObjectRequestOperation *operation, NSError *error)
     {
         NSLog(@"Failed with error: %@", [error localizedDescription]);
         [delegate onGogouRESTRequestFailure];
     }];
    
    [manager enqueueObjectRequestOperation:operation];
}

+ (void)performLoginRequestWith:(LoginInfo *)loginInfo
                       delegate:(id<RESTRequestListener>)delegate
{
    NSString *const REST_END = @"/gogou-web/services/subscriber/login";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Subscriber class]];
    
    [responseMapping addAttributeMappingsFromArray:@[@"Id",
                                                     @"emailAddress",
                                                     @"encodedPassword",
                                                     @"clearPassword",
                                                     @"userName",
                                                     @"isPurchaser",
                                                     @"age",
                                                     @"firstName",
                                                     @"lastName",
                                                     @"gender",
                                                     @"gcmToken",
                                                     @"imCode",
                                                     @"imToken"
                                                     ]];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *subscirberDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                              method:RKRequestMethodAny
                                                                                         pathPattern:REST_END
                                                                                             keyPath:nil
                                                                                         statusCodes:statusCodes];
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    [requestMapping addAttributeMappingsFromArray:@[@"emailAddress",
                                                    @"username",
                                                    @"password"]];
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping
                                                                                   objectClass:[LoginInfo class]
                                                                                   rootKeyPath:nil
                                                                                        method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:subscirberDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    
    NSLog([manager requestSerializationMIMEType]);
    
    
    // POST to create
    
    [manager postObject:loginInfo path:REST_END parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        
        
        NSLog(@"Log in successfull");
        Subscriber *subscriber = [mappingResult firstObject];
        [delegate onGogouRESTRequestSuccess:subscriber];
        
    }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                    [delegate onGogouRESTRequestFailure];
                    
                    
                }];

}

+ (void)performPostTripRequest:(Trip *)trip
                    imageArray:(NSMutableArray *) imageArray
                    subscriber:(Subscriber *) subscriber
                      delegate:(id<RESTRequestListener>) delegate
{
    RKLogConfigureByName("RestKit", RKLogLevelWarning);
    RKLogConfigureByName("RestKit/ObjectMapping", RKLogLevelTrace);
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
    
    NSString *const REST_END = @"/gogou-web/services/trip/create";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",@"error"]];
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodAny
                                                                                       pathPattern:REST_END
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    [requestMapping addAttributeMappingsFromDictionary:@{@"arrival" : @"arrival",
                                                         @"departure" : @"departure",
                                                         @"destination" : @"destination",
                                                         @"origin" : @"origin",
                                                         @"maxheight" : @"maxheight",
                                                         @"maxlength" : @"maxlength",
                                                         @"maxweight" : @"maxweight",
                                                         @"maxwidth" : @"maxwidth",
                                                         @"userName" : @"userName",
                                                         @"subscriberId" : @"subscriberId",
                                                         @"descriptions": @"description",
                                                         @"categoryNames" : @"categoryNames",
                                                         @"languageCode" : @"languageCode",
                                                         @"fileOutputs" : @"fileOutputs"}];
    
    // For any object of class Article, serialize into an NSMutableDictionary using the given mapping and nest
    // under the 'article' key path
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[Trip class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    //添加用户信息的http headers
    NSString *authorization = [NSString stringWithFormat:@"%@%@",subscriber.userName, subscriber.encodedPassword];
    NSString *base64authorization = [[authorization dataUsingEncoding:NSUTF8StringEncoding] base64EncodedStringWithOptions:0];
    NSString *authorizationString = @"basic ";
    authorizationString = [authorizationString stringByAppendingString:base64authorization];
    [manager.HTTPClient setDefaultHeader:@"Authorization" value:authorizationString];
    
    
    NSData *jsonPayload = [self getJSONpayloadFromObject:trip requestDescriptor:requestDescriptor];
    
    NSMutableURLRequest *request = [manager multipartFormRequestWithObject:nil
                                                                    method:RKRequestMethodPOST
                                                                      path:@"/gogou-web/services/trip/create"
                                                                parameters:nil
                                                 constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:jsonPayload
                                    name:@"trip"
                                fileName:@"dummy"
                                mimeType:RKMIMETypeJSON];
        NSString *fileStr = @"file";
        int count = 1;
        for (UIImage *img in imageArray){
            [formData appendPartWithFileData:UIImagePNGRepresentation(img)
                                        name:@"images"
                                    fileName:[[fileStr stringByAppendingString:[NSString stringWithFormat:@"%d",count]]stringByAppendingString:@".png"]
                                    mimeType:@"image/png"];
            count++;
        }
    }];
    
    RKObjectRequestOperation *operation = [manager objectRequestOperationWithRequest:request success:
                                           ^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                                               GenericResponse *genericResponse = [mappingResult firstObject];
                                               NSLog(@"%@",genericResponse.message);
                                               //[_loadview incrementPercentageBy:100];
                                               [delegate onGogouRESTRequestSuccess:genericResponse];
                                               
                                           }
                                           failure:
                                           ^(RKObjectRequestOperation *operation, NSError *error) {
                                               NSLog(@"Failed to get response from server...");
                                               
                                               [delegate onGogouRESTRequestFailure];
                                               
    
                                           }];
    
    [manager enqueueObjectRequestOperation:operation];
}

+ (void)performPostDemandRequest:(Demand *)demand
                      imageArray:(NSMutableArray *) imageArray
                      subscriber:(Subscriber *) subscriber
                        delegate:(id<RESTRequestListener>) delegate
{
    NSString *const REST_END = @"/gogou-web/services/demand/create";
    
    RKLogConfigureByName("RestKit", RKLogLevelWarning);
    RKLogConfigureByName("RestKit/ObjectMapping", RKLogLevelTrace);
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",@"error"]];
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodAny
                                                                                       pathPattern:REST_END
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    [requestMapping addAttributeMappingsFromDictionary:@{@"expectTime" : @"expectTime",
                                                         @"productname" : @"productname",
                                                         @"userName" : @"userName",
                                                         @"subscriberId" : @"subscriberId",
                                                         @"quantity" : @"quantity",
                                                         @"brand" : @"brand",
                                                         @"serviceFee" : @"serviceFee",
                                                         @"destination" : @"destination",
                                                         @"origin" : @"origin",
                                                         @"categoryName" : @"categoryName",
                                                         @"languageCode" : @"languageCode",
                                                         @"descriptions" : @"description"
                                                         //@"fileOutputs" : @"fileOutputs"
                                                         }];
    
    // For any object of class Article, serialize into an NSMutableDictionary using the given mapping and nest
    // under the 'article' key path
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[Demand class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    //添加用户信息的http headers
    NSString *authorization = [NSString stringWithFormat:@"%@%@",subscriber.userName, subscriber.encodedPassword];
    NSString *base64authorization = [[authorization dataUsingEncoding:NSUTF8StringEncoding] base64EncodedStringWithOptions:0];
    NSString *authorizationString = @"basic ";
    authorizationString = [authorizationString stringByAppendingString:base64authorization];
    [manager.HTTPClient setDefaultHeader:@"Authorization" value:authorizationString];
    
    
    NSData *jsonPayload = [self getJSONpayloadFromObject:demand requestDescriptor:requestDescriptor];
    
    NSMutableURLRequest *request = [manager multipartFormRequestWithObject:nil
                                                                    method:RKRequestMethodPOST
                                                                      path:REST_END parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:jsonPayload
                                    name:@"demand"
                                fileName:@"dummy"
                                mimeType:RKMIMETypeJSON];
        NSString *fileStr = @"file";
        int count = 1;
        for (UIImage *img in imageArray){
            [formData appendPartWithFileData:UIImagePNGRepresentation(img)
                                        name:@"images"
                                    fileName:[[fileStr stringByAppendingString:[NSString stringWithFormat:@"%d",count]]stringByAppendingString:@".png"]
                                    mimeType:@"image/png"];
            count++;
        }
    }];
    
    RKObjectRequestOperation *operation = [manager objectRequestOperationWithRequest:request success:
                                           ^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                                               GenericResponse *genericResponse = [mappingResult firstObject];
                                               NSLog(@"%@",genericResponse.message);
                                               
                                               [delegate onGogouRESTRequestSuccess:genericResponse];
                                               
                                           }
                                                                             failure:
                                           ^(RKObjectRequestOperation *operation, NSError *error) {
                                               NSLog(@"Failed to get response from server...");
                                               
                                               [delegate onGogouRESTRequestFailure];
                                               
                                           }];
    
    [manager enqueueObjectRequestOperation:operation];

}

+ (void)performGetCategoryListRequest:(NSString *)languageCode
                             delegate:(id<RESTRequestListener>)delegate
{
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
    [responseMapping addAttributeMappingsFromDictionary:@{@"id"         : @"Id",
                                                          @"name"       : @"name",
                                                          @"imagePath"  : @"imagePath",
                                                          @"displayName": @"displayName",
                                                          }];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodAny
                                                                                       pathPattern:nil
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL, @"/gogou-web/services/category/list?languageCode=", languageCode];
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request
                                                                        responseDescriptors:@[responseDescriptor]];
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result)
    {
        NSArray *objects = [result array];
        NSLog(@"There are %lu categories", (unsigned long)objects.count);
        [delegate onGogouRESTRequestSuccess:objects];
    }
     
                                     failure:^(RKObjectRequestOperation *operation, NSError *error)
    {
        NSLog(@"Error during category list request: %@", [error localizedDescription]);
        [delegate onGogouRESTRequestFailure];
    }];
    [operation start];
}

+ (void)performCreateOrderRequest:(Order *) order
                       subscriber:(Subscriber *) subscriber
                         delegate:(id<RESTRequestListener>)delegate{
    
    NSString *const REST_END = @"/gogou-web/services/order/create";
    
    RKLogConfigureByName("RestKit", RKLogLevelWarning);
    RKLogConfigureByName("RestKit/ObjectMapping", RKLogLevelTrace);
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",@"error"]];
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:REST_END keyPath:nil statusCodes:statusCodes];
    
    
    RKObjectMapping* descriptionMapping = [RKObjectMapping requestMapping];// objectClass == NSMutableDictionary
    [descriptionMapping addAttributeMappingsFromDictionary:@{@"minPrice": @"minPrice",
                                                             @"maxPrice": @"maxPrice",
                                                             @"productName": @"productName",
                                                             @"quantity": @"quantity",
                                                             @"categoryName": @"categoryName",
                                                             @"brand": @"brand",
                                                             @"origin": @"origin",
                                                             @"name": @"name",
                                                             @"languageCode": @"languageCode",
                                                             @"descriptions": @"description"
                                                             }];
    
    RKObjectMapping* addressMapping = [RKObjectMapping requestMapping];// objectClass == NSMutableDictionary
    [addressMapping addAttributeMappingsFromDictionary:@{@"userName" : @"userName",
                                                         @"city" : @"city",
                                                         @"company" : @"company",
                                                         @"firstName" : @"firstName",
                                                         @"lastName" : @"lastName",
                                                         @"postcode" : @"postcode",
                                                         @"state" : @"state",
                                                         @"streetAddress" : @"streetAddress",
                                                         @"telephone" : @"telephone",
                                                         @"country" : @"country"
                                                             }];
     
    
    //client --> server
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    [requestMapping addAttributeMappingsFromDictionary:@{
                                                         @"id" : @"id",
                                                         @"username" : @"username",
                                                         @"tripId" : @"tripId",
                                                         @"orderStatus" : @"orderStatus",
                                                         @"serviceFee" : @"serviceFee",
                                                         @"sellerId" : @"sellerId"
                                                         }];
    
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"orderDescriptions"
                                                                                   toKeyPath:@"orderDescriptions"
                                                                                 withMapping:descriptionMapping]];
    
    
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"address"
                                                                                   toKeyPath:@"address"
                                                                                 withMapping:addressMapping]];
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[Order class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    //添加用户信息的http headers
    NSString *authorization = [NSString stringWithFormat:@"%@%@",subscriber.userName, subscriber.encodedPassword];
    NSString *base64authorization = [[authorization dataUsingEncoding:NSUTF8StringEncoding] base64EncodedStringWithOptions:0];
    NSString *authorizationString = @"basic ";
    authorizationString = [authorizationString stringByAppendingString:base64authorization];
    [manager.HTTPClient setDefaultHeader:@"Authorization" value:authorizationString];
    
    
    NSLog([manager requestSerializationMIMEType]);
    
    // POST to create
    
    [manager postObject:order path:REST_END parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        
        NSLog(@"Request successfull");
        GenericResponse *genericResponse = [mappingResult firstObject];
        NSLog(@"%@",genericResponse.message);
        [delegate onGogouRESTRequestSuccess:genericResponse];

    
    }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                    NSLog(@"Failed to get response from server...");
                    
                    [delegate onGogouRESTRequestFailure];
                    
                }];

}

+ (void)performGetOrderListRequest:(OrderFilter *)orderFilter
                        subscriber:(Subscriber *) subscriber
                          delegate:(id<RESTRequestListener>)delegate{
    
    NSString *const REST_END = @"/gogou-web/services/order/list";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Order class]];
    
    RKValueTransformer *enumTransformer = [RKBlockValueTransformer valueTransformerWithValidationBlock:^BOOL(__unsafe_unretained Class sourceClass, __unsafe_unretained Class destinationClass) {
        // We transform a NSString into a NSNumber
        return ([sourceClass isSubclassOfClass:[NSString class]] && [destinationClass isSubclassOfClass:[NSNumber class]]);
        
    } transformationBlock:^BOOL(id inputValue, __autoreleasing id *outputValue, Class outputValueClass, NSError *__autoreleasing *error) {
        // Validate the input and output
        RKValueTransformerTestInputValueIsKindOfClass(inputValue, [NSString class], error);
        RKValueTransformerTestOutputValueClassIsSubclassOfClass(outputValueClass, [NSNumber class], error);
        
        
        // Perform the transformation
        NSDictionary *enumMap = @{
                                  @"PREORDERED" : @(PREORDERED),
                                  @"CONFIRMED" : @(CONFIRMED),
                                  @"ORDERED" : @(ORDERED),
                                  @"DELIVERED" : @(DELIVERED),
                                  @"COLLECTED" : @(COLLECTED),
                                  @"REFUNDED" : @(REFUNDED)
                                  };
        
        NSNumber *enumNum = enumMap[inputValue];
        
        *outputValue = enumNum;
        return YES;
    }];
    
    RKAttributeMapping *enumMapping = [RKAttributeMapping attributeMappingFromKeyPath:@"orderStatus" toKeyPath:@"orderStatus"];
    enumMapping.valueTransformer = enumTransformer;
    
    RKObjectMapping* descriptionMapping = [RKObjectMapping mappingForClass:[OrderDescription class]];// objectClass == NSMutableDictionary
    [descriptionMapping addAttributeMappingsFromDictionary:@{@"minPrice": @"minPrice",
                                                             @"maxPrice": @"maxPrice",
                                                             @"productName": @"productName",
                                                             @"quantity": @"quantity",
                                                             @"categoryName": @"categoryName",
                                                             @"brand": @"brand",
                                                             @"origin": @"origin",
                                                             @"name": @"name",
                                                             @"languageCode": @"languageCode",
                                                             @"descriptions": @"description"
                                                             }];
    
    RKObjectMapping* addressMapping = [RKObjectMapping mappingForClass:[Address class]];// objectClass == NSMutableDictionary
    [addressMapping addAttributeMappingsFromDictionary:@{@"userName" : @"userName",
                                                         @"city" : @"city",
                                                         @"company" : @"company",
                                                         @"firstName" : @"firstName",
                                                         @"lastName" : @"lastName",
                                                         @"postcode" : @"postcode",
                                                         @"state" : @"state",
                                                         @"streetAddress" : @"streetAddress",
                                                         @"telephone" : @"telephone",
                                                         @"country" : @"country"
                                                         }];
    
    
    
    [responseMapping addAttributeMappingsFromArray:@[@"id",
                                                     @"tripId",
                                                     @"username",
                                                     @"serviceFee",
                                                     @"currencyId",
                                                     @"currencyValue",
                                                     @"orderType",
                                                     @"orderTotal",
                                                     @"paymentType",
                                                     @"sellerId"
                                                     ]
     ];
    
    [responseMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"orderDescriptions"
                                                                                    toKeyPath:@"orderDescriptions"
                                                                                  withMapping:descriptionMapping]];
    
    [responseMapping addPropertyMapping:enumMapping];
    
    [responseMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"address"
                                                                                    toKeyPath:@"address"
                                                                                  withMapping:addressMapping]];
    
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:REST_END keyPath:nil statusCodes:statusCodes];
    
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary

    
    [requestMapping addAttributeMappingsFromDictionary:@{@"currentPage" : @"currentPage",
                                                         @"pageSize" : @"pageSize",
                                                         @"username" : @"username",
                                                         @"tripId" : @"tripId",
                                                         @"orderStatus" : @"orderStatus",
                                                         @"orderType" : @"orderType",
                                                         @"sellerId" : @"sellerId"
                                                         }];
    
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[OrderFilter class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    //添加用户信息的http headers
    NSString *authorization = [NSString stringWithFormat:@"%@%@",subscriber.userName, subscriber.encodedPassword];
    NSString *base64authorization = [[authorization dataUsingEncoding:NSUTF8StringEncoding] base64EncodedStringWithOptions:0];
    NSString *authorizationString = @"basic ";
    authorizationString = [authorizationString stringByAppendingString:base64authorization];
    [manager.HTTPClient setDefaultHeader:@"Authorization" value:authorizationString];
    
    NSLog([manager requestSerializationMIMEType]);
    
    [manager postObject:orderFilter path:REST_END parameters:nil
                success:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
                    
                    NSArray *objects = [result array];
                    NSLog(@"There are %lu orders", (unsigned long)objects.count);
                    [delegate onGogouRESTRequestSuccess:objects];
                }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                    NSLog(@"Error during order list request: %@", [error localizedDescription]);
                    [delegate onGogouRESTRequestFailure];
                }
     ];


    
}

+ (void)performGetOrderRequest:(NSNumber *)orderId
                      delegate:(id<RESTRequestListener>)delegate{
    
    NSString *const REST_END = @"/gogou-web/services/order/get?id=%@";
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Order class]];
    
    
    /*
    [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"id",
                                                          @"tripId" : @"tripId",
                                                          @"username" : @"username",
                                                          @"orderStatus" : @"orderStatus",
                                                          @"serviceFee" : @"serviceFee",
                                                          @"orderDescriptions" : @"orderDescriptions",
                                                          @"currencyId" : @"currencyId",
                                                          @"currencyValue" : @"currencyValue",
                                                          @"orderType" : @"orderType",
                                                          @"orderTotal" : @"orderTotal",
                                                          @"paymentType" : @"paymentType",
                                                          @"sellerId" : @"sellerId",
                                                          @"address" : @"address"
                                                          }];
     */
    RKObjectMapping* descriptionMapping = [RKObjectMapping requestMapping];// objectClass == NSMutableDictionary
    [descriptionMapping addAttributeMappingsFromDictionary:@{@"minPrice": @"minPrice",
                                                             @"maxPrice": @"maxPrice",
                                                             @"productName": @"productName",
                                                             @"quantity": @"quantity",
                                                             @"categoryName": @"categoryName",
                                                             @"brand": @"brand",
                                                             @"origin": @"origin",
                                                             @"name": @"name",
                                                             @"languageCode": @"languageCode",
                                                             @"descriptions": @"description"
                                                             }];
    
    RKObjectMapping* addressMapping = [RKObjectMapping requestMapping];// objectClass == NSMutableDictionary
    [addressMapping addAttributeMappingsFromDictionary:@{@"userName" : @"userName",
                                                         @"city" : @"city",
                                                         @"company" : @"company",
                                                         @"firstName" : @"firstName",
                                                         @"lastName" : @"lastName",
                                                         @"postcode" : @"postcode",
                                                         @"state" : @"state",
                                                         @"streetAddress" : @"streetAddress",
                                                         @"telephone" : @"telephone",
                                                         @"country" : @"country"
                                                         }];

    
    [responseMapping addAttributeMappingsFromArray:@[@"id",
                                                    @"tripId",
                                                    @"username",
                                                    @"orderStatus",
                                                    @"serviceFee",
                                                    @"currencyId",
                                                    @"currencyValue",
                                                    @"orderType",
                                                    @"orderTotal",
                                                    @"paymentType",
                                                    @"sellerId"
                                                    ]
     ];
    
    [responseMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"orderDescriptions"
                                                                                   toKeyPath:@"orderDescriptions"
                                                                                 withMapping:descriptionMapping]];
    
    
    [responseMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"address"
                                                                                   toKeyPath:@"address"
                                                                                 withMapping:addressMapping]];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping
                                                                                            method:RKRequestMethodGET
                                                                                       pathPattern:nil
                                                                                           keyPath:nil
                                                                                       statusCodes:statusCodes];
    
    NSString *pseudoRequest = [NSString stringWithFormat:@"%@%@", REST_SERVICES_URL, REST_END];
    NSString *requestString = [NSString stringWithFormat:pseudoRequest, orderId];
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request
                                                                        responseDescriptors:@[responseDescriptor]];
    
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result)
     {
         Order *orderResult = [result firstObject];
         [delegate onGogouRESTRequestSuccess:orderResult];
     }
                                     failure:^(RKObjectRequestOperation *operation, NSError *error)
     {
         NSLog(@"Failed with error: %@", [error localizedDescription]);
         [delegate onGogouRESTRequestFailure];
     }];
    [operation start];
}

+ (void)performUpdateOrderRequest:(Order *) order
                       subscriber:(Subscriber *) subscriber
                         delegate:(id<RESTRequestListener>)delegate{
    
    NSString *const REST_END = @"/gogou-web/services/order/update";
    
    RKLogConfigureByName("RestKit", RKLogLevelWarning);
    RKLogConfigureByName("RestKit/ObjectMapping", RKLogLevelTrace);
    RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",@"error"]];
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:REST_END keyPath:nil statusCodes:statusCodes];
    
    
    RKObjectMapping* descriptionMapping = [RKObjectMapping requestMapping];// objectClass == NSMutableDictionary
    [descriptionMapping addAttributeMappingsFromDictionary:@{@"minPrice": @"minPrice",
                                                             @"maxPrice": @"maxPrice",
                                                             @"productName": @"productName",
                                                             @"quantity": @"quantity",
                                                             @"categoryName": @"categoryName",
                                                             @"brand": @"brand",
                                                             @"origin": @"origin",
                                                             @"name": @"name",
                                                             @"languageCode": @"languageCode",
                                                             @"descriptions": @"description"
                                                             }];
    
    RKObjectMapping* addressMapping = [RKObjectMapping requestMapping];// objectClass == NSMutableDictionary
    [addressMapping addAttributeMappingsFromDictionary:@{@"userName" : @"userName",
                                                         @"city" : @"city",
                                                         @"company" : @"company",
                                                         @"firstName" : @"firstName",
                                                         @"lastName" : @"lastName",
                                                         @"postcode" : @"postcode",
                                                         @"state" : @"state",
                                                         @"streetAddress" : @"streetAddress",
                                                         @"telephone" : @"telephone",
                                                         @"country" : @"country"
                                                         }];
    
    
    //client --> server
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    [requestMapping addAttributeMappingsFromDictionary:@{@"id" : @"id",
                                                         @"username" : @"username",
                                                         @"tripId" : @"tripId",
                                                         @"orderStatus" : @"orderStatus",
                                                         @"serviceFee" : @"serviceFee",
                                                         @"sellerId" : @"sellerId"
                                                         }];
    
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"orderDescriptions"
                                                                                   toKeyPath:@"orderDescriptions"
                                                                                 withMapping:descriptionMapping]];
    
    
    [requestMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"address"
                                                                                   toKeyPath:@"address"
                                                                                 withMapping:addressMapping]];
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[Order class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    //添加用户信息的http headers
    NSString *authorization = [NSString stringWithFormat:@"%@%@",subscriber.userName, subscriber.encodedPassword];
    NSString *base64authorization = [[authorization dataUsingEncoding:NSUTF8StringEncoding] base64EncodedStringWithOptions:0];
    NSString *authorizationString = @"basic ";
    authorizationString = [authorizationString stringByAppendingString:base64authorization];
    [manager.HTTPClient setDefaultHeader:@"Authorization" value:authorizationString];
    
    
    NSLog([manager requestSerializationMIMEType]);
    
    // POST to create
    
    [manager postObject:order path:REST_END parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
        
        NSLog(@"Request successfull");
        GenericResponse *genericResponse = [mappingResult firstObject];
        NSLog(@"%@",genericResponse.message);
        [delegate onGogouOrderUpdateSuccess:genericResponse];
        
    }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                    NSLog(@"Failed to get response from server...");
                    
                    [delegate onGogouOrderUpdateFailure];
                    
                }];
    

}

+ (NSData*)getJSONpayloadFromObject:(NSObject*)object requestDescriptor:(RKRequestDescriptor*)requestDescriptor
{
    NSDictionary *paramObject = [RKObjectParameterization parametersWithObject:object
                                                             requestDescriptor:requestDescriptor
                                                                         error:nil];
    
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:paramObject
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    if(jsonData) {
        NSString *jsonString = [[NSString alloc] initWithData:jsonData
                                                     encoding:NSUTF8StringEncoding];
        NSLog(jsonString);
    }
    
    return jsonData;
}

@end
