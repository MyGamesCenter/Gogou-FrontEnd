//
//  DemandDetailTableViewController.m
//  Gogou
//
//  Created by xijunli on 16/4/9.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

#import "DemandDetailTableViewController.h"
#import "DetailPageTableViewCell0.h"
#import "DetailPageTableViewCell1.h"
#import "DetailPageTableViewCell2.h"
#import "DetailPageTableViewCell3.h"
#import "DetailPageTableViewCell4.h"
#import "PostTableViewCell2.h"
#import "FlatButton.h"
#import "ChatViewController.h"
#import "GoGouConstant.h"
#import <RestKit/RestKit.h>
#import "LGPhoto.h"
#import "Demand.h"
#import "ImageProcess.h"
#import "DataProcess.h"
#import "MBLoadingIndicator.h"
#import "RWDropdownMenu.h"
#import <RongIMKit/RongIMKit.h>

@interface DemandDetailTableViewController ()<LGPhotoPickerViewControllerDelegate,LGPhotoPickerBrowserViewControllerDataSource,LGPhotoPickerBrowserViewControllerDelegate>
{
    NSMutableArray *cellIdentifiers;
    FlatButton *linkButton;
    FlatButton *uploadPhotoButton;
    int showGrade;
    Demand *demand;
    
    NSMutableArray *thumbImageArray;
    NSMutableArray *originImageArray;
    NSMutableArray *imgBtnArray;
    NSMutableArray *clsBtnArray;
}

@property (nonatomic, assign) LGShowImageType showType;
@property (nonatomic, strong)NSMutableArray *LGPhotoPickerBrowserPhotoArray;
@property (nonatomic, strong)NSMutableArray *LGPhotoPickerBrowserURLArray;
@property (nonatomic, strong) MBLoadingIndicator *loadview;
@property (nonatomic, strong) NSArray *menuItems;
@end

@implementation DemandDetailTableViewController
@synthesize fromWhere;
@synthesize demandId;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.allowsSelection = NO;
    
    [self loadViewInit];
    
    [self setNavigationBar];
    
    [self cellIdentifiersInit];
    
    //初始化相册数组
    [self imageArrayInit];
    
    //向后台请求demand
    [self demandRequest:demandId];
    
    self.tableView.separatorColor = BACKGROUND_CORLOR;

}

- (void) loadViewInit{
    //Create the loader
    _loadview = [[MBLoadingIndicator alloc] init];
    
    //NOTE: Any extra loader can be done here, including sizing, colors, animation speed, etc
    //      Pre-start changes will not be animated.
    [_loadview setLoaderStyle:MBLoaderFullCircle];
    [_loadview setLoaderSize:MBLoaderTiny];
    [_loadview setStartPosition:MBLoaderLeft];
    [_loadview setWidth:4];
    //[_loadview setOuterLoaderBuffer:0];
    [_loadview offsetCenterYBy:-100.0f];
    [_loadview setAnimationSpeed:MBLoaderSpeedFast];
    
    //Start the loader
    [self.loadview start];
    
    //Add the loader to our view
    [self.view addSubview:self.loadview];
}

- (void) imageArrayInit{
    thumbImageArray = [[NSMutableArray alloc]init];
    self.LGPhotoPickerBrowserPhotoArray = [[NSMutableArray alloc]init];
}

- (void) cellIdentifiersInit{
    cellIdentifiers = [[NSMutableArray alloc]init];
    for (int i=0;i<5;i++)
    {
        NSString *tmp = @"DetailPageTableViewCell";
        tmp = [tmp stringByAppendingString:[NSString stringWithFormat:@"%d",i]];
        [cellIdentifiers addObject:tmp];
    }
    [cellIdentifiers addObject:@"PostTableViewCell2"];
}

- (NSArray *)menuItems
{
    if (!_menuItems)
    {
        UIImage *img1 = [ImageProcess reSizeImage:[UIImage imageNamed:@"favorite"] toSize:CGSizeMake(35, 35)];
        //UIImage *img2 = [ImageProcess reSizeImage:[UIImage imageNamed:@"sign_yuan"] toSize:CGSizeMake(35, 35)];
        _menuItems =
        @[
          [RWDropdownMenuItem itemWithText:NSLocalizedString(@"收藏需求", nil) image:img1 action:^{
              
          }],
          
          ];
    }
    return _menuItems;
}

- (void)setNavigationBar{
    self.navigationItem.title = NSLocalizedString(@"需求详情", nil);
    if (fromWhere == 0)
    {
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[[UIImage imageNamed:@"more"] imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] style:UIBarButtonItemStylePlain target:self action:@selector(presentMenuFromNav:)];
        
    }
}

- (void)presentMenuFromNav:(id)sender
{
    RWDropdownMenuCellAlignment alignment = RWDropdownMenuCellAlignmentCenter;
    if (sender == self.navigationItem.leftBarButtonItem)
    {
        alignment = RWDropdownMenuCellAlignmentLeft;
    }
    else
    {
        alignment = RWDropdownMenuCellAlignmentRight;
    }
    
    [RWDropdownMenu presentFromViewController:self withItems:self.menuItems align:alignment style:RWDropdownMenuStyleTranslucent navBarImage:[sender image] completion:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSInteger rowCount;
    if (fromWhere == 0)
    {
        switch (section) {
            case 0:
                rowCount = 1;
                break;
            case 1:
                rowCount = 10;
                break;
                
            default:
                break;
        }
    }
    else{
        switch (section) {
            case 0:
                rowCount = 1;
                break;
            case 1:
                rowCount = 9;
                break;
                
            default:
                break;
        }
    }
    return rowCount;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    switch (section) {
        case 0:
            return NSLocalizedString(@"账户信息", nil);
            break;
        case 1:
            return NSLocalizedString(@"需求信息", nil);
            break;
    }
    return nil;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    CGFloat height = 44.0f;
    if (indexPath.section == 0 && indexPath.row == 0)
        height = 76.0f;
    if (indexPath.section == 1 && indexPath.row == 7)
        height = 175.0f;
    if (indexPath.section == 1 && indexPath.row == 8)
        height = 285.0f;
    if (indexPath.section == 1 && indexPath.row == 9)
        height = 55.0f;
    return height;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    
    if (indexPath.section == 0 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[0] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[0]];
        DetailPageTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.headPortraitImage.image = [UIImage imageNamed:@"user_large.png"];
        tmp.AccountNameLabel.text = demand.userName;
        
        showGrade = 5;
        switch (showGrade) {
            case 5:
                tmp.star5.image = [UIImage imageNamed:@"star2.png"];
                //break;
            case 4:
                tmp.star4.image = [UIImage imageNamed:@"star2.png"];
                //break;
            case 3:
                tmp.star3.image = [UIImage imageNamed:@"star2.png"];
                //break;
            case 2:
                tmp.star2.image = [UIImage imageNamed:@"star2.png"];
                //break;
            case 1:
                tmp.star1.image = [UIImage imageNamed:@"star2.png"];
                break;
            default:
                break;
        }
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"收货时间", nil);
        tmp.ValueLabel.text = demand.expectTime;
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"产品名", nil);
        tmp.ValueLabel.text = demand.productname;
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 2){
        
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"数量", nil);
        tmp.ValueLabel.text = [demand.quantity stringValue];
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 3){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"品牌", nil);
        tmp.ValueLabel.text = demand.brand;
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 4){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"服务费", nil);
        float tmpf = [demand.serviceFee floatValue];
        tmpf *= 100.0;
        NSString *serviceFeeString = [NSString stringWithFormat:@"%@%@",[DataProcess decimalwithFormat:@"0.00" floatV:tmpf],@"%"];
        tmp.ValueLabel.text = serviceFeeString;
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 5){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"原产地", nil);
        tmp.ValueLabel.text = demand.origin;
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 6){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"收货地", nil);
        tmp.ValueLabel.text = demand.destination;
        cell = tmp;
    }
    
    
    
    if (indexPath.section == 1 && indexPath.row == 7){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[2] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[2]];
        DetailPageTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[2] forIndexPath:indexPath];
        tmp.detailDescriptionTextView.text =  demand.descriptions;
        cell = tmp;
    }
    
    if (indexPath.section == 1 &&indexPath.row == 8){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[5] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[5]];
        PostTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[5] forIndexPath:indexPath];
        [tmp awakeFromNib];
        
        
        if (!uploadPhotoButton)
        {
            uploadPhotoButton = tmp.button;
        }
        if (thumbImageArray)
        {
            for (int i=0;i<thumbImageArray.count && i<6;i++){
                [tmp.picArray[i] setBackgroundImage:thumbImageArray[i] forState:UIControlStateNormal];
                [tmp.picArray[i] addTarget:self action:@selector(picButtonTap:) forControlEvents:UIControlEventTouchDown];
            }
        }
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 9){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[3] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[3]];
        DetailPageTableViewCell3 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[3] forIndexPath:indexPath];
        //[tmp.linkButton setTitleFButton:@"联系买家"];
        if (!linkButton)
        {
        linkButton = tmp.linkButton;
        [linkButton setBackgroundImage:[ImageProcess imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
        [linkButton setBackgroundImage:[ImageProcess imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
        [linkButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        //linkButton.layer.borderColor = [UIColor colorWithRed:57.0f/255 green:125.0f/255 blue:194.0f/255 alpha:1].CGColor;
        //linkButton.layer.borderWidth = 1.0f;
        //linkButton.layer.cornerRadius = 5.0f;
        [linkButton addTarget:self action:@selector(buttonClick:) forControlEvents:UIControlEventTouchUpInside];
        }
        cell = tmp;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 1 && indexPath.row == 8){
        if ([cell respondsToSelector:@selector(setSeparatorInset:)])
        {
            [cell setSeparatorInset:UIEdgeInsetsZero];
        }
        if ([cell respondsToSelector:@selector(setPreservesSuperviewLayoutMargins:)])
        {
            [cell setPreservesSuperviewLayoutMargins:NO];
        }
        if ([cell respondsToSelector:@selector(setLayoutMargins:)])
        {
            [cell setLayoutMargins:UIEdgeInsetsZero];
        }
    }
    
    
}

- (void)picButtonTap:(id) sender{
    [self pushPhotoBroswerWithStyle:LGShowImageTypeImageBroswer];
}
#pragma mark - LGPhotoPickerViewControllerDelegate

- (void)pickerViewControllerDoneAsstes:(NSArray *)assets isOriginal:(BOOL)original{
    
    for (LGPhotoAssets *photo in assets) {
        //缩略图
        [thumbImageArray addObject:photo.thumbImage];
        //原图
        [originImageArray addObject:photo.originImage];
        LGPhotoPickerBrowserPhoto *photo1 = [[LGPhotoPickerBrowserPhoto alloc] init];
        photo1.photoImage = photo.originImage;
        [self.LGPhotoPickerBrowserPhotoArray addObject:photo1];
    }
}

- (void)pushPhotoBroswerWithStyle:(LGShowImageType)style{
    LGPhotoPickerBrowserViewController *BroswerVC = [[LGPhotoPickerBrowserViewController alloc] init];
    BroswerVC.delegate = self;
    BroswerVC.dataSource = self;
    BroswerVC.showType = style;
    self.showType = style;
    [self presentViewController:BroswerVC animated:YES completion:nil];
}



#pragma mark - LGPhotoPickerBrowserViewControllerDataSource

- (NSInteger)photoBrowser:(LGPhotoPickerBrowserViewController *)photoBrowser numberOfItemsInSection:(NSUInteger)section{if (self.showType == LGShowImageTypeImageBroswer) {
    return self.LGPhotoPickerBrowserPhotoArray.count;
} else if (self.showType == LGShowImageTypeImageURL) {
    return self.LGPhotoPickerBrowserURLArray.count;
} else {
    NSLog(@"非法数据源");
    return 0;
}
}

- (id<LGPhotoPickerBrowserPhoto>)photoBrowser:(LGPhotoPickerBrowserViewController *)pickerBrowser photoAtIndexPath:(NSIndexPath *)indexPath{
    if (self.showType == LGShowImageTypeImageBroswer) {
        return [self.LGPhotoPickerBrowserPhotoArray objectAtIndex:indexPath.item];
    } else if (self.showType == LGShowImageTypeImageURL) {
        return [self.LGPhotoPickerBrowserURLArray objectAtIndex:indexPath.item];
    } else {
        NSLog(@"非法数据源");
        return nil;
    }
}

/**
 聊天按钮点击事件
 */
-(void) buttonClick:(id)sender{
    /*
    ChatViewController *cvc = [ChatViewController new];
    
     cvc.chatFriend = [[Account alloc] init];
     cvc.chatFriend.userName = getTrip.username;
     cvc.chatFriend.Id = getTrip.accountId;
     cvc.tripId = getTrip.Id;
     cvc.receiverPic = ownerPic;
     cvc.picStr = owenrPicUrl;
     cvc.pathFlag = [NSNumber numberWithInt:1];//1：跳转过去，可以创建订单
    [self.navigationController pushViewController:cvc animated:YES];
    */
    
    
    RCConversationViewController *conversationVC = [[RCConversationViewController alloc]init];
    conversationVC.conversationType = 1;
    conversationVC.targetId = demand.subscriberId;
    conversationVC.userName = demand.userName;
    conversationVC.title = demand.userName;
    [self.navigationController pushViewController:conversationVC animated:YES];
    
}

/*

-(void)onSelectedTableRow:(RCConversationModelType)conversationModelType conversationModel:(RCConversationModel *)model atIndexPath:(NSIndexPath *)indexPath
{
    RCConversationViewController *conversationVC = [[RCConversationViewController alloc]init];
    conversationVC.conversationType =model.conversationType;
    conversationVC.targetId = model.targetId;
    conversationVC.userName =model.conversationTitle;
    conversationVC.title = model.conversationTitle;
    [self.navigationController pushViewController:conversationVC animated:YES];
    
    
}
*/

/*****
 ***** demandRequest
 *****/
- (void)demandRequest:(NSNumber *)Id{
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Demand class]];
    
    //dict中key为json中字段名称，value是实体中字段名称
    //response中返回的json数据，是从json字段映射到实体字段
    [responseMapping addAttributeMappingsFromDictionary:@{@"id"  : @"Id",
                                                          @"expectTime" : @"expectTime",
                                                          @"productname" : @"productname",
                                                          @"userName" : @"userName",
                                                          @"subscriberId" : @"subscriberId",
                                                          @"quantity" : @"quantity",
                                                          @"brand" : @"brand",
                                                          @"serviceFee" : @"serviceFee",
                                                          @"destination" : @"destination",
                                                          @"origin" : @"origin",
                                                          @"categoryName" : @"categoryName",
                                                          @"languageCode" : @"languageCode",
                                                          @"description" : @"descriptions",
                                                          @"fileOutputs" : @"fileOutputs"
                                                          }];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
    
    NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/demand/get?id=",[Id stringValue]];
    
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        demand = [result firstObject];
        for (NSString *data in demand.fileOutputs){
            UIImage *img = [ImageProcess imageConvertedFromData:data];
           
            if (img != nil)
            {
                //添加图片到thumbImageArray
                [thumbImageArray addObject:img];
                //添加图片到放大的图片array
                LGPhotoPickerBrowserPhoto *photo1 = [[LGPhotoPickerBrowserPhoto alloc] init];
                photo1.photoImage = img;
                [self.LGPhotoPickerBrowserPhotoArray addObject:photo1];
            }
            
        }
        
        [self.tableView reloadData];
        [_loadview finish];
    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        NSLog(@"Failed with error: %@", [error localizedDescription]);
    }];
    [operation start];
}


@end
