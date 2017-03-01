//
//  RequirementPreviewTableViewController.m
//  worldshopping2.0
//
//  Created by xijunli on 16/1/3.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import "RequirementPreviewTableViewController.h"
#import "DetailPageTableViewCell0.h"
#import "DetailPageTableViewCell1.h"
#import "DetailPageTableViewCell2.h"
#import "DetailPageTableViewCell3.h"
#import "DetailPageTableViewCell4.h"
#import "DetailPageTableViewCell5.h"

@interface RequirementPreviewTableViewController ()
{
    NSMutableArray *cellIdentifiers;
    UIButton *submitButton;//确认提交按钮
}

@end

@implementation RequirementPreviewTableViewController

@synthesize photo;
@synthesize addRequ;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationItem.title = @"需求发布预览";
    cellIdentifiers = [[NSMutableArray alloc]init];
    for (int i=0;i<6;i++)
    {
        NSString *tmp = @"DetailPageTableViewCell";
        tmp = [tmp stringByAppendingString:[NSString stringWithFormat:@"%d",i]];
        [cellIdentifiers addObject:tmp];
    }
    self.tableView.allowsSelection = NO;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 10;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    CGFloat height = 44.0f;
    if (indexPath.row == 7)
        height = 128.0f;
    if (indexPath.row == 8)
        height = 175.0f;
    if (indexPath.row == 9)
        height = 64.0f;
    return height;
}

-(UIImage *)imageWithColorToButton:(UIColor *)color
{
    CGRect rect = CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return image;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [dateFormatter setTimeZone:timeZone];
    
    NSString* receiptTimeStr = [dateFormatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:[addRequ.receiptTime doubleValue] / 1000.0]];
    
    if (indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"类别";
        tmp.ValueLabel.text = addRequ.itemTypeName;//@"香水";
        cell = tmp;
    }
    
    if (indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"品牌";
        tmp.ValueLabel.text = addRequ.bankName;//@"香奈儿";
        cell = tmp;
    }
    
    if (indexPath.row == 2){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"预期收货时间";
        tmp.ValueLabel.text = receiptTimeStr;//@"2015-1-1";
        cell = tmp;
    }
    if (indexPath.row == 3){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"收货地";
        tmp.ValueLabel.text = addRequ.receipePlaceName;//@"北京";
        cell = tmp;
    }
    
    if (indexPath.row == 4){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"原产地";
        tmp.ValueLabel.text = addRequ.originName;//@"巴黎";
        cell = tmp;
    }
    
    if (indexPath.row == 5){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"数量";
        tmp.ValueLabel.text = [NSString stringWithFormat:@"%@ 件", addRequ.num];//@"1 件";
        cell = tmp;
    }
    
    if (indexPath.row == 6){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"出价";
        tmp.ValueLabel.text = [NSString stringWithFormat:@"%@ 元", addRequ.price];//@"800 元";
        cell = tmp;
    }
    
    if (indexPath.row == 7){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[2] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[2]];
        DetailPageTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[2] forIndexPath:indexPath];
        tmp.detailDescriptionTextView.text = addRequ.detail;//@"whatever";
        cell = tmp;
    }
    
    if (indexPath.row == 8){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[4] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[4]];
        DetailPageTableViewCell4 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[4]];
        //tmp.imageButton的背景图片设置成上一个页面传过来的图像
        [tmp.imageButton setBackgroundImage:photo forState:UIControlStateNormal];
        [tmp.imageButton setTitle:@"" forState:UIControlStateNormal];
        cell = tmp;
    }
    
    if (indexPath.row == 9){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[5] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[5]];
        DetailPageTableViewCell5 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[5] forIndexPath:indexPath];
        //[tmp.linkButton setTitleFButton:@"联系卖家"];
        if (!submitButton)
        {
            submitButton = tmp.button;
            [submitButton  setBackgroundImage:[self imageWithColorToButton:[UIColor colorWithRed:70.0f/255 green:138.0f/255 blue:207.0f/255 alpha:1]] forState:UIControlStateNormal];
            [submitButton  setBackgroundImage:[self imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
            [submitButton  setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            submitButton .layer.borderColor = [UIColor colorWithRed:57.0f/255 green:125.0f/255 blue:194.0f/255 alpha:1].CGColor;
            submitButton .layer.borderWidth = 1.0f;
            submitButton .layer.cornerRadius = 5.0f;
            [submitButton  addTarget:self action:@selector(buttonClick:) forControlEvents:UIControlEventTouchUpInside];
        }
        cell = tmp;
    }
    
    return cell;
}

- (void) buttonClick:(id)sender{
    //上传图片
    [self uplodaImage];
    
    [self addRequirement];
}

-(void)addRequirement{
}

-(void)uplodaImage{
    
    
}

@end
