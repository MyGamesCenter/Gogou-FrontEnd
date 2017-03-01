//
//  PostTableViewCell2.m
//  worldshopping2.0
//
//  Created by xijunli on 16/1/2.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import "PostTableViewCell2.h"

@interface PostTableViewCell2 (){
    
}

@end

@implementation PostTableViewCell2


- (void)awakeFromNib {
    // Initialization code
    _picArray = [NSMutableArray arrayWithObjects:_pic1,_pic2,_pic3,_pic4,_pic5,_pic6, nil];
    _closeArray = [NSMutableArray arrayWithObjects:_close1,_close2,_close3,_close4,_close5,_close6,nil];
    
    _label.text = NSLocalizedString(@"图片预览", nil);
    
    [_button setBackgroundImage:[UIImage imageNamed:@"camera.png"] forState:UIControlStateNormal];
    
    int index = 0;
    for (UIButton* picButton in _picArray)
    {
        picButton.titleLabel.text = @"";
        picButton.tag = index;
        index++;
        [picButton setBackgroundImage:nil forState:UIControlStateNormal];
    }
    
    index =0;
    for (UIButton* closeButton in _closeArray)
    {
        closeButton.tag = index;
        closeButton.hidden = YES;
        index++;
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
