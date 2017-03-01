//
//  PostTableViewCell2.h
//  worldshopping2.0
//
//  Created by xijunli on 16/1/2.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FlatButton.h"

@interface PostTableViewCell2 : UITableViewCell
@property (weak, nonatomic) IBOutlet FlatButton *button;

@property (weak, nonatomic) IBOutlet UIButton *pic1;
@property (weak, nonatomic) IBOutlet UIButton *pic2;
@property (weak, nonatomic) IBOutlet UIButton *pic3;
@property (weak, nonatomic) IBOutlet UIButton *pic4;
@property (weak, nonatomic) IBOutlet UIButton *pic5;
@property (weak, nonatomic) IBOutlet UIButton *pic6;

@property (weak, nonatomic) IBOutlet UIButton *close1;
@property (weak, nonatomic) IBOutlet UIButton *close2;
@property (weak, nonatomic) IBOutlet UIButton *close3;
@property (weak, nonatomic) IBOutlet UIButton *close4;
@property (weak, nonatomic) IBOutlet UIButton *close5;
@property (weak, nonatomic) IBOutlet UIButton *close6;

@property (weak, nonatomic) IBOutlet UILabel *label;

@property (nonatomic) NSMutableArray *picArray;
@property (nonatomic) NSMutableArray *closeArray;
@end
