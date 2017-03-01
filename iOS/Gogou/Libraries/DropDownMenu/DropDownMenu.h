//
//  DropDownMenu.h
//  worldshopping2.0
//
//  Created by xijunli on 15/12/24.
//  Copyright © 2015年 SJTU. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DropDownMenu : UIView<UITableViewDelegate,UITableViewDataSource>
{
    NSMutableArray *dropDownMenuData;
    UITableView *dropDownMenu;
    BOOL showDropDownMenu;
    CGRect oldFrame,newFrame;
    UIColor *lineColor,*listBgColor;//下拉框的边框色、背景色
    CGFloat lineWidth;               //下拉框边框粗细
    //NSNumber *tripId;
    //NSString *chatFriendName;
    //NSNumber *chatFriendId;
    //NSNumber *pathFlag;//来向标记；1：仅从“行程详情”来时，才可以创建订单；0：不能创建订单，从“需求详情”进来；2：从“我的消息”进来，不显示右上角下拉菜单
    
    //UIImage *sellerPic;//卖家的头像
    //UIImage *buyerPic;//买家的头像
}

@property (nonatomic, strong) NSMutableArray* dropDownMenuData;

@property (nonatomic, strong) UITableView *dropDownMenu;

@property (nonatomic) BOOL showDropDownMenu;

@property (nonatomic) CGRect oldFrame,newFrame;

@property (nonatomic)UIColor *lineColor,*listBgColor;
@property (nonatomic)CGFloat lineWidth;

/*
@property (nonatomic, retain) NSNumber *tripId;
@property (nonatomic, retain) NSString *chatFriendName;
@property (nonatomic, retain) NSNumber *chatFriendId;

@property (nonatomic, retain) NSNumber *pathFlag;

@property (nonatomic, retain) UIImage *sellerPic;//卖家的头像
@property (nonatomic, retain) UIImage *buyerPic;//买家的头像
 */

-(void)drawView;
-(void)setShow:(BOOL)b;
-(void)dropDown;
@end
