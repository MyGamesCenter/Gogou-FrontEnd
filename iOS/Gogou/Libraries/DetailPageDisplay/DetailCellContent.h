//
//  DetailCellContent.h
//  worldshopping2.0
//
//  Created by xijunli on 15/12/10.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DetailCellContent : NSObject

@property(nonatomic,strong) id Key;
@property(nonatomic,strong) id Value;

- (DetailCellContent*) initWithKey:(id) key andValue:(id)value;


@end
