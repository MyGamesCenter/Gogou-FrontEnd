//
//  DataProcess.m
//  Gogou
//
//  Created by xijunli on 16/4/10.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "DataProcess.h"
#import "GoGouConstant.h"
#import "RNEncryptor.h"
#import "RNDecryptor.h"

@implementation DataProcess

/**
 *数码转换
 */
+ (NSString *) decimalwithFormat:(NSString *)format  floatV:(float)floatV
{
    NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
    
    [numberFormatter setPositiveFormat:format];
    
    return  [numberFormatter stringFromNumber:[NSNumber numberWithFloat:floatV]];
}

/**
 *对明文密码进行加密
 * @param string 明文密码
 * return 密文密码
 */
+ (NSString *)cipherWithString:(NSString *)string{
    NSError *error = nil;
    NSData *data = [string dataUsingEncoding:NSASCIIStringEncoding];
    NSData *ciphertext = [RNEncryptor encryptData:data
                                     withSettings:kRNCryptorAES256Settings
                                         password:SEED_VALUE
                                            error:&error];
    NSString *retString = [ciphertext base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
    return retString;
}

/**
 *对密文密码进行解密
 * @param string 密文密码
 * return 明文密码
 */
+ (NSString*) decipherWithString:(NSString *)string{
    
    //To do
    
    NSData *ciphertext = [[NSData alloc] initWithBase64EncodedString:string options:NSDataBase64DecodingIgnoreUnknownCharacters];
    
    // Decryption
    NSError *error = nil;
    NSData *plaintext = [RNDecryptor decryptData:ciphertext withPassword:SEED_VALUE error:&error];
    if (error != nil) {
        NSLog(@"ERROR:%@", error);
        return @"ERROR";
    }
    
    NSString *retString = [[NSString alloc]initWithData:plaintext encoding:NSASCIIStringEncoding];
    
    return retString;
}

@end
