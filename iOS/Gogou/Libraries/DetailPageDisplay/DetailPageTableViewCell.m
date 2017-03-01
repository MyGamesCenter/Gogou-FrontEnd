//
//  DetailPageTableViewCell.m
//  WorldShopping
//
//  Created by xijunli on 15/11/28.
//  Copyright © 2015年 laimeng. All rights reserved.
//

#import "DetailPageTableViewCell.h"
#import "DetailCellContent.h"
@interface DetailPageTableViewCell(){
    CGFloat controllerSpacing;
    CGFloat leadingSpacing;
    CGFloat verticalSapcing;
}

@end
@implementation DetailPageTableViewCell

-(instancetype) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self)
    {
        //删除iOS默认风格cell中的子控件
        for (id subview in [self.contentView subviews]){
            [subview removeFromSuperview];
        }
    }
    return self;
}

/*- (void)drawRect:(CGRect)rect{
    CGContextRef context = UIGraphicsGetCurrentContext();
    //画线
    CGPoint aPoints[2];//坐标点
    CGRect cellRect = self.contentView.frame;
    aPoints[0] =CGPointMake(110, cellRect.size.height*0.1);//坐标1
    aPoints[1] =CGPointMake(110, cellRect.size.height*0.9);//坐标2
    //CGContextAddLines(CGContextRef c, const CGPoint points[],size_t count)
    //points[]坐标数组，和count大小
    UIColor *lineColor = [UIColor lightGrayColor];
    CGContextSetStrokeColorWithColor(context, lineColor.CGColor);
    CGContextAddLines(context, aPoints, 2);//添加线
    CGContextDrawPath(context, kCGPathStroke); //根据坐标绘制路径
}*/

#pragma mark 初始化cell中的内容
-(void) initSubView{
    CGRect cellRect = [UIScreen mainScreen].bounds;
    CGFloat label1Width = 0.3 * cellRect.size.width;
    CGFloat label2Width = 0.6 * cellRect.size.width;
    CGSize textSize=[_label2.text boundingRectWithSize:CGSizeMake(label2Width, MAXFLOAT)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:DetailPageCellTableViewCellLabelFontSize]}
                                               context:nil].size;
    
    CGFloat labelHeight = textSize.height;
    
    controllerSpacing = 0.04 * cellRect.size.width;
    leadingSpacing = (cellRect.size.width - label1Width - label2Width - controllerSpacing) / 2.0;
    verticalSapcing = 10.0;
    
    
    CGFloat label1X = cellRect.origin.x + leadingSpacing;
    CGFloat label1Y = verticalSapcing;

    CGRect label1Rect = CGRectMake(label1X, label1Y, label1Width, labelHeight);

    [_label1 setFrame:label1Rect];
    _label1.font = [UIFont fontWithName:@"Arial" size:DetailPageCellTableViewCellLabelFontSize];
    [self.contentView addSubview:_label1];
    
    
    CGFloat label2X = CGRectGetMaxX(_label1.frame) + controllerSpacing;
    CGFloat label2Y = label1Y;
    _label2.numberOfLines = 0;
    _label2.lineBreakMode = NSLineBreakByCharWrapping;
    CGRect label2Rect = CGRectMake(label2X, label2Y, label2Width, labelHeight);
    [_label2 setFrame:label2Rect];
    
    _label2.font = [UIFont fontWithName:@"Arial" size:DetailPageCellTableViewCellLabelFontSize];
    _label2.layer.cornerRadius = 4.0f;
    _label2.layer.borderWidth = 0.5;
    _label2.layer.borderColor = [UIColor lightGrayColor].CGColor;
    _label2.layer.backgroundColor = [UIColor lightGrayColor].CGColor;
    //_label2.backgroundColor = [UIColor lightGrayColor];
    
    [self.contentView addSubview:_label2];
}

-(void) setCellContentWithInfo:(DetailCellContent *)dcc{
   
    _label1 = [[UILabel alloc]init];
    _label1.text = dcc.Key;
    _label1.textAlignment =  NSTextAlignmentLeft;
    _label2 = [[UILabel alloc]init];
    _label2.textAlignment = NSTextAlignmentCenter;
    _label2.text = dcc.Value;
    
    [self initSubView];
    _height = CGRectGetMaxY(_label2.frame);
    _height += verticalSapcing;
}


@end
