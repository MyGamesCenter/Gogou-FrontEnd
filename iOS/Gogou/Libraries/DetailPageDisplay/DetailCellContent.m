//
//  DetailCellContent.m
//  worldshopping2.0
//
//  Created by xijunli on 15/12/10.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import "DetailCellContent.h"

@implementation DetailCellContent

@synthesize Key;
@synthesize Value;

- (DetailCellContent*) initWithKey:(id)key andValue:(id)value
{
    if (self = [super init])
    {
        self.Key = key;
        self.Value = value;
    }
    return self;
}

@end
