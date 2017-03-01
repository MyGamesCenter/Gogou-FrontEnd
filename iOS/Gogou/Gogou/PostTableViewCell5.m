//
//  PostTableViewCell5.m
//  Gogou
//
//  Created by xijunli on 16/5/3.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "PostTableViewCell5.h"
#import "DropDownMenu.h"

@interface PostTableViewCell5 ()

@property (nonatomic, strong) DropDownMenu *dropDownMenuView;

@end

@implementation PostTableViewCell5

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    CGFloat menuWidth = 118;
    CGFloat menuRowHeight = 35;
    CGFloat menuX = 126 ;
    CGFloat menuY = 47;
    
    [self.valueButton addTarget:self action:@selector(dropDown) forControlEvents:UIControlEventTouchUpInside];
    
    _dropDownMenuView = [DropDownMenu alloc];
    _dropDownMenuView = [_dropDownMenuView initWithFrame:CGRectMake(menuX, menuY, menuWidth, menuRowHeight)];
    
    [self.view addSubview:_dropDownMenuView];
     
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void) dropDown{
    [_dropDownMenuView dropDown];
}

@end
