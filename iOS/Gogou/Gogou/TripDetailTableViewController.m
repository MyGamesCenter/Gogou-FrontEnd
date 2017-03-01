//
//  TripDetailTableViewController.m
//  Gogou
//
//  Created by xijunli on 16/4/3.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

#import "TripDetailTableViewController.h"
#import "DetailPageTableViewCell0.h"
#import "DetailPageTableViewCell1.h"
#import "DetailPageTableViewCell2.h"
#import "DetailPageTableViewCell3.h"
#import "DetailPageTableViewCell4.h"
#import "CreateOrderTableViewController.h"
#import "PostTableViewCell2.h"
#import "FlatButton.h"
#import "Trip.h"
#import "ChatViewController.h"
#import "GoGouConstant.h"
#import <RestKit/RestKit.h>
#import "LGPhoto.h"
#import "ImageProcess.h"
#import "MBLoadingIndicator.h"
#import "RWDropdownMenu.h"
#import <RongIMKit/RongIMKit.h>


@interface TripDetailTableViewController ()<LGPhotoPickerViewControllerDelegate,LGPhotoPickerBrowserViewControllerDataSource,LGPhotoPickerBrowserViewControllerDelegate>
{
    NSMutableArray *cellIdentifiers;
    FlatButton *linkButton;
    FlatButton *uploadPhotoButton;
    int showGrade;
    Trip *trip;
    
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

@implementation TripDetailTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.allowsSelection = NO;
    
    [self loadViewInit];

    [self cellIdentifiersInit];
    
    
    //初始化相册数组
    [self imageArrayInit];
    
    //向后台请求trip
    [self tripRequest:_tripId];
    
    
    
    //初始化navigation bar
    [self setNavigationBar];
    
    self.tableView.separatorColor = BACKGROUND_CORLOR;

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
        UIImage *img2 = [ImageProcess reSizeImage:[UIImage imageNamed:@"sign_yuan"] toSize:CGSizeMake(35, 35)];
        _menuItems =
        @[
          [RWDropdownMenuItem itemWithText:NSLocalizedString(@"收藏行程", nil) image:img1 action:^{
              
          }],
          [RWDropdownMenuItem itemWithText:NSLocalizedString(@"创建订单", nil) image:img2 action:^{
              CreateOrderTableViewController *vc = [[CreateOrderTableViewController alloc]init];
              NSLog(@"%@",trip.origin);
              [vc setTrip:trip];
              [self.navigationController pushViewController:vc animated:YES];
          }],
          ];
    }
    return _menuItems;
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

- (void)setNavigationBar{
    self.navigationItem.title = NSLocalizedString(@"行程详情", nil);
    if (_fromWhere == 0)
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


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 4;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    switch (section) {
        case 0:
            return NSLocalizedString(@"账户信息", nil);
            break;
        case 1:
            return NSLocalizedString(@"行程信息", nil);
            break;
        case 2:
            return NSLocalizedString(@"携带物品信息", nil);
            break;
        case 3:
            return @"";
            break;
    }
    return nil;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSInteger rowCount;
    if (_fromWhere == 0)
    {
        switch (section) {
            case 0:
                rowCount = 1;
                break;
            case 1:
                rowCount = 4;
                break;
            case 2:
                rowCount = 5;
                break;
            case 3:
                rowCount = 3;
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
                rowCount = 4;
                break;
            case 2:
                rowCount = 5;
                break;
            case 3:
                rowCount = 2;
                break;
            default:
                break;
        }
    }
    return rowCount;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    CGFloat height = 44.0f;
    if (indexPath.section == 0 && indexPath.row == 0)
        height = 76.0f;
    if (indexPath.section == 3 && indexPath.row == 0)
        height = 175.0f;
    if (indexPath.section == 3 && indexPath.row == 2)
        height = 55.0f;
    if (indexPath.section == 3 && indexPath.row == 1)
        height = 285.0f;
    return height;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell;
    
    
    if (indexPath.section == 0 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[0] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[0]];
        DetailPageTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.headPortraitImage.image = [UIImage imageNamed:@"user_large.png"];
        tmp.AccountNameLabel.text = trip.userName;
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
        tmp.KeyLabel.text = NSLocalizedString(@"出发地", nil);
        tmp.ValueLabel.text = trip.origin;
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"目的地", nil);
        tmp.ValueLabel.text = trip.destination;
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 2){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"出发时间", nil);
        tmp.ValueLabel.text = trip.departure;
        cell = tmp;
    }
    if (indexPath.section == 1 && indexPath.row == 3){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"到达时间", nil);
        tmp.ValueLabel.text = trip.arrival;
        cell = tmp;
    }
    
    if (indexPath.section == 2 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"最大长度", nil);
        tmp.ValueLabel.text = [trip.maxheight stringValue];
        cell = tmp;
    }
    if (indexPath.section == 2 && indexPath.row == 1){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"最大宽度", nil);
        tmp.ValueLabel.text = [trip.maxwidth stringValue];
        cell = tmp;
    }
    
    if (indexPath.section == 2 && indexPath.row == 2){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"最大高度", nil);
        tmp.ValueLabel.text = [trip.maxheight stringValue];
        cell = tmp;
    }
    
    if (indexPath.section == 2 && indexPath.row == 3){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"最大重量", nil);
        tmp.ValueLabel.text = [trip.maxweight stringValue];
        cell = tmp;
    }
    
    
    if (indexPath.section == 2 && indexPath.row == 4){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        DetailPageTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"物品类型", nil);;
        if (trip.categoryNames.count != 0){
            tmp.ValueLabel.text = trip.categoryNames[0];
        }
        else{
            tmp.ValueLabel.text = NSLocalizedString(@"所有类型", nil);;
        }
            
        cell = tmp;
    }
    
    if (indexPath.section == 3 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[2] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[2]];
        DetailPageTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[2] forIndexPath:indexPath];
        tmp.detailDescriptionTextView.text = trip.descriptions;
        cell = tmp;
    }
    
    if (indexPath.section == 3 && indexPath.row == 1){
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
    
    if (indexPath.section == 3 && indexPath.row == 2){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[3] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[3]];
        DetailPageTableViewCell3 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[3] forIndexPath:indexPath];
        //[tmp.linkButton setTitleFButton:@"联系卖家"];
        if (!linkButton){
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
    if (indexPath.section == 3 && indexPath.row == 1){
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
    conversationVC.targetId = trip.subscriberId;
    conversationVC.userName = trip.userName;
    conversationVC.title = trip.userName;
    [self.navigationController pushViewController:conversationVC animated:YES];
    
}




/*****
 ***** tripRequest
 *****/
- (void)tripRequest:(NSNumber *)Id{
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Trip class]];
    
    //server --> client
    [responseMapping addAttributeMappingsFromDictionary:@{@"id"  : @"Id",
                                                          @"arrival" : @"arrival",
                                                          @"departure" : @"departure",
                                                          @"destination" : @"destination",
                                                          @"origin" : @"origin",
                                                          @"maxheight" : @"maxheight",
                                                          @"maxlength" : @"maxlength",
                                                          @"maxweight" : @"maxweight",
                                                          @"maxwidth" : @"maxwidth",
                                                          @"userName" : @"userName",
                                                          @"subscriberId" : @"subscriberId",
                                                          @"description": @"descriptions",
                                                          @"categoryNames" : @"categoryNames",
                                                          @"languageCode" : @"languageCode",
                                                          @"fileOutputs" : @"fileOutputs"}];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
    
    NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/trip/get?id=",[Id stringValue]];
    
    
    NSLog(@"%@",requestString);
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
    RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
    [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
        trip = [result firstObject];
        for (NSString *data in trip.fileOutputs){
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
        //[_loadview incrementPercentageBy:100];
        [self.tableView reloadData];
        [_loadview finish];

    } failure:^(RKObjectRequestOperation *operation, NSError *error) {
        NSLog(@"Failed with error: %@", [error localizedDescription]);
    }];
    [operation start];
}

@end
