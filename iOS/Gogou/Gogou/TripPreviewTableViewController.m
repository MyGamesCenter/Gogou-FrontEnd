//
//  TripPreviewTableViewController.m
//  worldshopping2.0
//
//  Created by xijunli on 16/1/3.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#import "TripPreviewTableViewController.h"
#import "DetailPageTableViewCell0.h"
#import "DetailPageTableViewCell1.h"
#import "DetailPageTableViewCell2.h"
#import "DetailPageTableViewCell3.h"
#import "DetailPageTableViewCell4.h"
#import "DetailPageTableViewCell5.h"
#import <RestKit/RestKit.h>
#import "GenericResponse.h"


@interface TripPreviewTableViewController (){
    NSMutableArray *cellIdentifiers;
    UIButton *submitButton;//确认提交按钮
}

@end

@implementation TripPreviewTableViewController

@synthesize addTrip;
@synthesize photo;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationItem.title = @"行程发布预览";
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
    return 11;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    CGFloat height = 44.0f;
    if (indexPath.row == 8)
        height = 128.0f;
    if (indexPath.row == 9)
        height = 175.0f;
    if (indexPath.row == 10)
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
    
    NSString* arriveTime = [dateFormatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:[addTrip.arriveTime doubleValue] / 1000.0]];
    NSString* gooffTime = [dateFormatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:[addTrip.gooffTime doubleValue] / 1000.0]];
    
    if (indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"出发地";
        tmp.ValueLabel.text = addTrip.departureName;//@"上海";
        cell = tmp;
    }
    
    if (indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"目的地";
        tmp.ValueLabel.text = addTrip.destinationName;//@"巴黎";
        cell = tmp;
    }
    
    if (indexPath.row == 2){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"出发时间";
        tmp.ValueLabel.text = gooffTime;//@"2015-1-1";
        cell = tmp;
    }
    if (indexPath.row == 3){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"到达时间";
        tmp.ValueLabel.text = arriveTime;//@"2015-1-1";
        cell = tmp;
    }
    
    if (indexPath.row == 4){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"服务费";
        //NSString *servicePrice = [NSString stringWithFormat:@"%@ 元",getTrip.servicePrice];
        //tmp.ValueLabel.text = [NSString stringWithFormat:@"%@ 元",getTrip.servicePrice];
        tmp.ValueLabel.text = [NSString stringWithFormat:@"%@ 元", addTrip.servicePrice];//@"12元";
        cell = tmp;
    }
    
    if (indexPath.row == 5){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"优先携带物品";
        tmp.ValueLabel.text = addTrip.precedenceName;//@"手机";
        cell = tmp;
    }
    
    if (indexPath.row == 6){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"最大重量";
        //NSString *maxWeight = [NSString stringWithFormat:@"%@ kg",getTrip.maxWeight];
        //tmp.ValueLabel.text = [NSString stringWithFormat:@"%@ kg",getTrip.maxWeight];
        tmp.ValueLabel.text = [NSString stringWithFormat:@"%@ 千克", addTrip.maxWeight];//@"12 千克";
        cell = tmp;
    }
    
    if (indexPath.row == 7){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = @"最大体积";
        //tmp.ValueLabel.text = getTrip.maxVolume;
        tmp.ValueLabel.text = [NSString stringWithFormat:@"%@ 立方米", addTrip.maxVolume];//@"1*1*1 立方米";
        cell = tmp;
    }
    
    if (indexPath.row == 8){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[2] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[2]];
        DetailPageTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[2] forIndexPath:indexPath];
        tmp.detailDescriptionTextView.text = addTrip.detail;//@"whatever";
        cell = tmp;
    }
    
    if (indexPath.row == 9){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[4] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[4]];
        DetailPageTableViewCell4 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[4]];
        //tmp.imageButton的背景图片设置成上一个页面传过来的图像
        [tmp.imageButton setBackgroundImage:photo forState:UIControlStateNormal];
        [tmp.imageButton setTitle:@"" forState:UIControlStateNormal];
        cell = tmp;
    }
    
    if (indexPath.row == 10){
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
    //添加行程
    //[self postTripInfo];
}

/*

-(void)postTripInfo{
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GenericResponse class]];
    [responseMapping addAttributeMappingsFromArray:@[@"message",@"error"]];
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:@"/gogou-web/services/trip/create" keyPath:nil statusCodes:statusCodes];
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    [requestMapping addAttributeMappingsFromArray:@[@"arrival",@"departure",@"destination",@"origin",@"description",@"maxheight",@"maxlength",@"maxweight",@"maxwidth",@"username",@"categoryNames",@"languageCode",@"fileOutputs"]];
    
    // For any object of class Article, serialize into an NSMutableDictionary using the given mapping and nest
    // under the 'article' key path
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[TripModel class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:@"http://104.196.39.44"]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    
    NSLog([manager requestSerializationMIMEType]);
    
    
    TripModel *trip = [TripModel new];
    trip.arrival = @"2016-03-13 12:00";
    trip.departure = @"2016-03-13 11:00";
    trip.destination = @"";
    trip.origin = @"";
    trip.userName = @"admin";
    trip.categoryNames = [[NSMutableArray alloc]initWithArray:@[@"wine",@"baby"]];
    trip.languageCode = @"zh-s";
    
    
    
    // POST to create
    //[manager postObject:loginInfo path:@"/gogou-web/services/subscriber/login" parameters:nil success:nil failure:nil];
    [manager postObject:trip path:@"/gogou-web/services/trip/create" parameters:nil success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
            }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                }];
}
*/
-(void)uplodaImage{
    }


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    }

@end
