//
//  MainScreenViewController.m
//  Gogou
//
//  Created by xijunli on 16/1/21.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "MainScreenViewController.h"
#import "UIView+MJExtension.h"
#import "MJRefresh.h"
#import "FlatButton.h"
#import "DOPDropDownMenu.h"
#import "BBBadgeBarButtonItem.h"
#import "PostTripTableViewController.h"
#import "PostRequirementTableViewController.h"
#import "LoginViewController.h"
#import "TripTableViewCell.h"
#import "DemandDetailTableViewController.h"
#import "DemandTableViewCell.h"
#import "TripFilter.h"
#import "Demand.h"
#import "Trip.h"
#import "DemandFilter.h"
#import "TripDetailTableViewController.h"
#import "GoGouConstant.h"
#import "GoGouColors.h"
#import "GGCategory.h"
#import "GGCity.h"
#import "GGTime.h"
#import "DateTranslation.h"
#import "Subscriber.h"
#import "CacheManager.h"
#import "OAuth2Login.h"
#import "RESTRequestUtils.h"
#import <RestKit/RestKit.h>
#import <RongIMKit/RongIMKit.h>

//static const CGFloat MJDuration = 1.0;

@interface MainScreenViewController () <DOPDropDownMenuDataSource,
                                        DOPDropDownMenuDelegate,
                                        UITableViewDataSource,
                                        UITableViewDelegate,
                                        UIActionSheetDelegate,
                                        RESTRequestListener>{
    
    NSMutableArray *tripList; //行程列表信息
    NSMutableArray *demandList; //需求列表信息
    NSMutableArray *categoryList;//类别信息列表
    NSMutableArray *originList;//商品原产地列表
    NSMutableArray *destinationList;//商品收获地列表
    NSMutableArray *tripOriginList;//行程出发地列表
    NSMutableArray *tripDestinationList;//行程目的地列表
    NSMutableArray *arrivalTimeList;//行程到达时间列表
    
    TripFilter *tripFilter;
    DemandFilter *demandFilter;
    NSInteger currentPage;
    
    int newNotice;//1：新消息；2：新订单
    
    NSString *subscriberId;
    UISegmentedControl *segmentedControl;
    UIButton *customButton;
    UIImage *ownerPic;//登录者头像
    
    NSString *currentAccount;//当前登录账号ß
    
    BOOL _isTrip;
    BOOL _loginState;
}
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet FlatButton *bottomButton;

@property (nonatomic)BBBadgeBarButtonItem *barButton;
@property (nonatomic, strong) DOPDropDownMenu *filterMenu;
@property (nonatomic) int isRefresh;//刷新标志位
@property (nonatomic) int isCategoryListReady;
@property (nonatomic) int isOriginListReady;
@property (nonatomic) int isDestinationListReady;
@property (nonatomic) int isTripOriginListReady;
@property (nonatomic) int isTripDestinationListReady;
@property (nonatomic) int isArrivalTimeReady;
@end

@implementation MainScreenViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    _isTrip = NO;
    [self dataListInit];

    [self setUpFilterMenu];
    [self setUpBottomButton];
    
    [self.view addSubview:_filterMenu];
    
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    
    tripList = [[NSMutableArray alloc]init];
    demandList = [[NSMutableArray alloc]init];
    
    [self pullUpLoadInit];
    
    if (_isTrip)
        [self tripSegmentControlSelected];
    else
        [self demandSegmentControlSelected];
    
}

- (void) dataListInit
{
    GGCategory *category = [GGCategory new];
    category.name = NSLocalizedString(@"类别", nil);
    category.displayName = NSLocalizedString(@"类别", nil);
    GGCity *origin = [GGCity new];
    origin.name = NSLocalizedString(@"原产地", nil);
    GGCity *collectLocation = [GGCity new];
    collectLocation.name = NSLocalizedString(@"收货地", nil);
    GGCity *departure = [GGCity new];
    departure.name = NSLocalizedString(@"出发地", nil);
    GGCity *destination = [GGCity new];
    destination.name = NSLocalizedString(@"目的地", nil);
    GGTime *time = [GGTime new];
    time.name = NSLocalizedString(@"到达时间", nil);
    
    categoryList = [[NSMutableArray alloc]init];
    [categoryList addObject:category];
    originList  = [[NSMutableArray alloc]init];
    [originList addObject:origin];
    destinationList  = [[NSMutableArray alloc]init];
    [destinationList addObject:collectLocation];
    tripDestinationList = [[NSMutableArray alloc]init];
    [tripDestinationList addObject:destination];
    tripOriginList = [[NSMutableArray alloc]init];
    [tripOriginList addObject:departure];
    arrivalTimeList = [[NSMutableArray alloc]init];
    [arrivalTimeList addObject:time];
    
    tripFilter = [TripFilter new];
    tripFilter.currentPage = [NSNumber numberWithInt:1];
    tripFilter.pageSize = [NSNumber numberWithInt:10];
    tripFilter.userName = nil;
    tripFilter.departure = @"desc";
    tripFilter.ratingOrder = @"desc";
    
    demandFilter = [DemandFilter new];
    demandFilter.currentPage = [NSNumber numberWithInt:1];
    demandFilter.pageSize = [NSNumber numberWithInt:10];
    demandFilter.userName = nil;
    demandFilter.serviceFeeOrder = @"desc";
    demandFilter.ratingOrder = @"desc";
    
    currentPage = 1;
}

- (void)viewWillAppear:(BOOL)animated
{
    [self setUpNavigationBar];
    
    Subscriber *subscriber = [[CacheManager sharedManager] subscriber];
    BOOL isLogin = [[CacheManager sharedManager] isLogin];
    
    if (isLogin)//已经登录过
    {
        _loginState = YES;
        [customButton setImage:[UIImage imageNamed:@"user"] forState:UIControlStateNormal];
        
        NSString *token = subscriber.imToken;
        [[RCIM sharedRCIM] connectWithToken:token
                                    success:^(NSString *userId)
        {
            //设置用户信息提供者,页面展现的用户头像及昵称都会从此代理取
            subscriberId = userId;
            [[RCIM sharedRCIM] setUserInfoDataSource:self];
            NSLog(@"Login successfully with userId: %@.", userId);
            /*
             dispatch_async(dispatch_get_main_queue(), ^{
             ChatListViewController *chatListViewController = [[ChatListViewController alloc]init];
             [self.navigationController pushViewController:chatListViewController animated:YES];
             });
             */
        }
                                      error:^(RCConnectErrorCode status)
        {
            NSLog(@"login error status: %ld.", (long)status);
        }
                             tokenIncorrect:^
        {
            NSLog(@"token 无效 ，请确保生成token 使用的appkey 和初始化时的appkey 一致");
        }];
    }
}


/**
 *rongyun的protocal 需要自己实现
 *此方法中要提供给融云用户的信息，建议缓存到本地，然后改方法每次从您的缓存返回
 */
- (void)getUserInfoWithUserId:(NSString *)userId completion:(void(^)(RCUserInfo* userInfo))completion
{
    //此处为了演示写了一个用户信息
    RCUserInfo *user = [[RCUserInfo alloc]init];
    Subscriber *subscriber = [[CacheManager sharedManager] subscriber];
    user.userId = userId;
    user.name = subscriber.userName;
    //user.portraitUri =@"";
    return completion(user);
}


#pragma mark UITableView + 下拉刷新 默认
- (void)pullDownRefresh
{
    __unsafe_unretained __typeof(self) weakSelf = self;
    
    // 设置回调（一旦进入刷新状态就会调用这个refreshingBlock）
    self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        [weakSelf loadNewData];
    }];
    
    // 马上进入刷新状态
    [self.tableView.mj_header beginRefreshing];
}

#pragma mark UITableView + 上拉刷新 默认
- (void)pullUpLoadInit
{
    //[self pullDownRefresh];
    
    __unsafe_unretained __typeof(self) weakSelf = self;
    
    // 设置回调（一旦进入刷新状态就会调用这个refreshingBlock）
    self.tableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
        [weakSelf loadMoreData];
    }];
    
    //[self.tableView.mj_footer beginRefreshing];
}

#pragma mark - 数据处理相关
#pragma mark 下拉刷新数据
- (void)loadNewData
{
    
    if (_isTrip)
    {
        _isRefresh = 1;
        tripFilter.currentPage = [NSNumber numberWithInteger:1];
        [self tripListRequest:tripFilter];
    }
    else
    {
        _isRefresh = 1;
        demandFilter.currentPage = [NSNumber numberWithInteger:1];
        [self demandListRequest:demandFilter];
    }
    /*
     // 2.模拟2秒后刷新表格UI（真实开发中，可以移除这段gcd代码）
     dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(MJDuration * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
     // 刷新表格
     
     
     // 拿到当前的下拉刷新控件，结束刷新状态
     [self.tableView.mj_header endRefreshing];
     });*/
}

- (void)loadMoreData
{
    if (_isTrip)
    {
        _isRefresh = 0;
        tripFilter.currentPage = [NSNumber numberWithInteger:[tripFilter.currentPage integerValue]+1];
        [self tripListRequest:tripFilter];
    }
    else
    {
        _isRefresh = 0;
        demandFilter.currentPage = [NSNumber numberWithInteger:[demandFilter.currentPage integerValue]+1];
        [self demandListRequest:demandFilter];
    }
    /*
     // 1.添加假数据
     for (int i = 0; i<5; i++) {
     [self.data insertObject:MJRandomData atIndex:0];
     }
     */
    /*
     // 2.模拟2秒后刷新表格UI（真实开发中，可以移除这段gcd代码）
     dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(MJDuration * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
     // 刷新表格
     [self.tableView reloadData];
     
     // 拿到当前的上拉刷新控件，结束刷新状态
     [self.tableView.mj_footer endRefreshing];
     });
     */
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setUpNavigationBar
{
    //创建segmentedControl
    CGFloat segmentedControlWidth = self.navigationController.navigationBar.frame.size.width * 0.4;
    CGFloat segmentedControlHeight = self.navigationController.navigationBar.frame.size.height * 0.7;
    segmentedControl = [[UISegmentedControl alloc] initWithFrame:CGRectMake(0, 0, segmentedControlWidth, segmentedControlHeight)];
    [segmentedControl insertSegmentWithImage:[UIImage imageNamed:@"shopping"] atIndex:0 animated:YES];
    [segmentedControl insertSegmentWithImage:[UIImage imageNamed:@"plane"] atIndex:1 animated:YES];
    segmentedControl.selectedSegmentIndex = _isTrip ? 1 : 0;
    segmentedControl.multipleTouchEnabled = NO;
    [segmentedControl addTarget:self action:@selector(segmentControlValueChanged:) forControlEvents:UIControlEventValueChanged];
    
    self.navigationItem.titleView = segmentedControl;
    //self.navigationItem.title = @"主页";
    
    // If you want your BarButtonItem to handle touch event and click, use a UIButton as customView
    customButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 45, 45)];
    // Add your action to your button
    [customButton addTarget:self action:@selector(leftButtonClick:) forControlEvents:UIControlEventTouchUpInside];
    // Customize your button as you want, with an image if you have a pictogram to display for example
    [customButton setImage:[UIImage imageNamed:@"user_gray"] forState:UIControlStateNormal];
    
    // Then create and add our custom BBBadgeBarButtonItem
    self.barButton = [[BBBadgeBarButtonItem alloc] initWithCustomUIButton:customButton];
    //self.barButton = [[BBBadgeBarButtonItem alloc]initWithImage:nil style:UIBarButtonItemStylePlain target:self action:@selector(leftButtonClick:)];
    //[self.barButton setStyle:UIBarButtonItemStylePlain];
    // Set a value for the badge
    
    self.barButton.badgeOriginX = 19;
    self.barButton.badgeOriginY = 0;
    
    // Add it as the leftBarButtonItem of the navigation bar
    self.navigationItem.leftBarButtonItem = self.barButton;
    
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor whiteColor];

}

- (void) setUpBottomButton{
    
    
    [_bottomButton setBackgroundImage:[self imageWithColorToButton:APP_MAIN_CORLOR] forState:UIControlStateNormal];
    [_bottomButton setBackgroundImage:[self imageWithColorToButton:ROYAL_BLUE] forState:UIControlStateHighlighted];
    
    [_bottomButton setTitle:NSLocalizedString(@"我要发布", nil) forState:UIControlStateNormal];
    [_bottomButton addTarget:self action:@selector(bottomButtonTouchUpInside:) forControlEvents:UIControlEventTouchUpInside];
    
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

- (void) bottomButtonTouchUpInside:(id)sender{
    
    
    UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:nil
                                                       delegate:self
                                              cancelButtonTitle:NSLocalizedString(@"取消", nil)
                                         destructiveButtonTitle:nil
                                              otherButtonTitles:NSLocalizedString(@"我买东西", nil), NSLocalizedString(@"我要旅行", nil),  nil];
    
    [sheet showFromRect:self.view.frame inView:self.view animated:YES];
    
    
}

- (void)actionSheet:(UIActionSheet *)actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == actionSheet.cancelButtonIndex) {
        return;
    }
    if (_loginState)
    {
    Subscriber *subscriber = [[CacheManager sharedManager] subscriber];
    switch (buttonIndex) {
        case 0:
        {
            PostRequirementTableViewController *vc = [[PostRequirementTableViewController alloc]init];
            vc.categoryList = categoryList;
            vc.subscriberId = subscriberId;
            vc.userName = subscriber.userName;
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 1:
        {
            PostTripTableViewController *vc = [[PostTripTableViewController alloc]init];
            vc.categoryList = categoryList;
            vc.subscriberId = subscriberId;
            vc.userName = subscriber.userName;
            /*
            for (GGCategory *item in categoryList){
                [vc.categoryList addObject:item.name];
            }
             */
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
            
    }
    }
    else{
        [self leftButtonClick:nil];
    }
}

- (void) setUpFilterMenu
{
    CGFloat filterMenuX = 0;
    CGFloat filterMenuY = 64;
    CGFloat filterMenuHeight = 40;
    _filterMenu = [[DOPDropDownMenu alloc] initWithOrigin:CGPointMake(filterMenuX, filterMenuY) andHeight:filterMenuHeight];
    _filterMenu.textSelectedColor = APP_MAIN_CORLOR;
    _filterMenu.dataSource = self;
    _filterMenu.delegate = self;
    
    [RESTRequestUtils performGetCategoryListRequest:NSLocalizedString(@"language_code", "zh-s")
                                           delegate:self];
    [self tripOriginListRequest:@"zh-s"];
    [self tripDestinationListRequest:@"zh-s"];
    [self arrivalTimeListRequest:@"zh-s"];
    [self originListRequest:@"zh-s"];
    [self destinationListRequest:@"zh-s"];
}


- (void) segmentControlValueChanged:(UISegmentedControl*)sender
{
    switch (sender.selectedSegmentIndex) {
        case 0:
            _isTrip = NO;
            [self demandSegmentControlSelected];
            
            break;
        case 1:
            _isTrip = YES;
            [self tripSegmentControlSelected];
        default:
            break;
    }
    [_filterMenu reloadData];
}

- (void) tripSegmentControlSelected
{
    _isTrip = YES;
    
    [self pullDownRefresh];
}

- (void) demandSegmentControlSelected{
    
    _isTrip = NO;
    
    [self pullDownRefresh];
}

- (void) leftButtonClick:(id) sender
{
    
    //获取当前的登录状态
    if (!_loginState) {
        LoginViewController *lvc = [LoginViewController new];
        //[self presentViewController:lvc animated:YES completion:^{}];
        [self.navigationController pushViewController:lvc animated:YES];
    }else {
        //已处于登录状态，不需要做操作
    }
}

- (NSInteger)numberOfColumnsInMenu:(DOPDropDownMenu *)menu
{
    int retVal;
    if (_isTrip)
        retVal = 4;
    else
        retVal = 3;
    return  retVal;
}

- (NSInteger)menu:(DOPDropDownMenu *)menu numberOfRowsInColumn:(NSInteger)column
{
    NSInteger rows = 0;
    if (!_isTrip)
    {

        switch (column) {
            case 0:
                rows = categoryList.count;
                break;
            case 1:
                rows = originList.count;
                break;
            case 2:
                rows = destinationList.count;
                break;
            default:
                break;
        }
    }
    else
    {
        switch (column) {
            case 0:
                rows = tripOriginList.count;
                break;
            case 1:
                rows = tripDestinationList.count;
                break;
            case 2:
                rows = categoryList.count;
                break;
            case 3:
                rows = arrivalTimeList.count;
                break;
            default:
                break;
        }
    }
    return rows;
}

- (NSString *)menu:(DOPDropDownMenu *)menu titleForRowAtIndexPath:(DOPIndexPath *)indexPath
{
    NSString *title;
    if (!_isTrip)
    {
        switch (indexPath.column)
        {
            case 0: {
                GGCategory *tmp = [categoryList objectAtIndex:indexPath.row];
                title = [tmp displayName];
            }
                break;
            case 1: {
                GGCity *tmp = [originList objectAtIndex:indexPath.row];
                title = tmp.name;
            }
                break;
            case 2: {
                GGCity *tmp = [destinationList objectAtIndex:indexPath.row];
                title = tmp.name;
            }
                break;
            default:
                break;
        }
        
    }
    else
    {
        switch (indexPath.column)
        {
            case 0: {
                GGCity *tmp = [tripOriginList objectAtIndex:indexPath.row];
                title = tmp.name;
            }
                break;
            case 1: {
                GGCity *tmp = [tripDestinationList objectAtIndex:indexPath.row];
                title = tmp.name;
            }
                break;
            case 2: {
                GGCategory *tmp = [categoryList objectAtIndex:indexPath.row];
                title = [tmp displayName];
            }
                break;
            case 3: {
                GGTime *tmp = [arrivalTimeList objectAtIndex:indexPath.row];
                title = tmp.name;
            }
                break;
            default:
                break;
                
        }
        
    }
    return title;
}

- (void)menu:(DOPDropDownMenu *)menu didSelectRowAtIndexPath:(DOPIndexPath *)indexPath
{

    NSInteger tripCategorySelectedRow = 0;
    NSInteger tripDepartureSelectedRow = 0;
    NSInteger tripDestinationSelectedRow = 0;
    NSInteger tripOriginSelectedRow = 0;
    NSInteger tripArrivalTimeSelectedRow = 0;
    NSInteger demandOriginSelectedRow = 0;
    NSInteger demandDestinaltionSelectedRow = 0;
    NSInteger demandCategorySelectedRow = 0;
    //所有过滤器初始默认选择第一项
    
    if (_isTrip){
        
        switch (indexPath.column) {
            case 0: {
                tripOriginSelectedRow = indexPath.row;
            }
                break;
            case 1: {
                tripDestinationSelectedRow = indexPath.row;
            }
                break;
            case 2: {
                tripCategorySelectedRow = indexPath.row;
            }
                break;
            case 3: {
                tripArrivalTimeSelectedRow = indexPath.row;
            }
                break;
            default:
                break;
        }
        
        tripFilter = [TripFilter new];
        tripFilter.currentPage = [NSNumber numberWithInt:1];
        tripFilter.pageSize = [NSNumber numberWithInt:30];
        tripFilter.userName = nil;
        tripFilter.departure = @"desc";
        tripFilter.ratingOrder = @"desc";
        GGCategory *category = [categoryList objectAtIndex:tripCategorySelectedRow];
        tripFilter.categoryName = category.name;
        /*
        GGCity *destinationCity = [tripDestinationList objectAtIndex:tripDestinationSelectedRow];
        tripFilter.destination = destinationCity.name;
        GGCity *originCity = [tripOriginList objectAtIndex:tripOriginSelectedRow];
        tripFilter.origin = originCity.name;
         */
        [self pullDownRefresh];
    }
    else{
        switch (indexPath.column) {
            case 0: {
                demandCategorySelectedRow = indexPath.row;
            }
                break;
            case 1: {
                demandOriginSelectedRow = indexPath.row;
            }
                break;
            case 2: {
                demandDestinaltionSelectedRow = indexPath.row;
            }
                break;
            default:
                break;
        }
        
        demandFilter = [DemandFilter new];
        demandFilter.currentPage = [NSNumber numberWithInt:1];
        demandFilter.pageSize = [NSNumber numberWithInt:30];
        demandFilter.userName = nil;
        demandFilter.serviceFeeOrder = @"desc";
        demandFilter.ratingOrder = @"desc";
        GGCategory *category = [categoryList objectAtIndex:demandCategorySelectedRow];
        demandFilter.categoryName = category.name;
        /*
        GGCity *city = [originList objectAtIndex:demandOriginSelectedRow];
        demandFilter.origin = city.name;
        */
        [self pullDownRefresh];
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    NSUInteger count = 0;
    
    if (_isTrip)
    {
        if (tripList)
            count = tripList.count;
    }
    else
    {
        if (demandList)
            count = demandList.count;
    }
    //NSLog(@"rows count : %ld",count);
    return count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell;
    static NSString *tripCellIdentifier = @"TripTableViewCell";
    static NSString *demandCellIdentifier = @"DemandTableViewCell";
    //static BOOL isTripRegNib = NO;
    //static BOOL isDemandRegNib = NO;
    if (_isTrip == 1){
        
        /*if (!isTripRegNib){
            UINib *nib = [UINib nibWithNibName:tripCellIdentifier bundle:nil];
            [tableView registerNib:nib forCellReuseIdentifier:tripCellIdentifier];
            isTripRegNib = YES;

        }*/
        UINib *nib = [UINib nibWithNibName:tripCellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:tripCellIdentifier];
        //isTripRegNib = YES;
        
        TripTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:tripCellIdentifier];
        
        if (tripList.count != 0){
            Trip *trip = [tripList objectAtIndex:indexPath.section];
            [tmp setupCell:trip];
        }
        else{
            Trip *trip = [Trip new];
            [tmp setupCell:trip];
        }
        
        cell = tmp;
    }
    else{
        //cellIdentifier = [NSString stringWithFormat:@"DemandTableViewCell"];
        /*
        if (!isDemandRegNib){
            UINib *nib = [UINib nibWithNibName:demandCellIdentifier bundle:nil];
            [tableView registerNib:nib forCellReuseIdentifier:demandCellIdentifier];
            isDemandRegNib = YES;
        }
         */
        UINib *nib = [UINib nibWithNibName:demandCellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:demandCellIdentifier];
        DemandTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:demandCellIdentifier];
        
        if (demandList.count != 0){
            
            NSString *imagePath;
            Demand *demand = [demandList objectAtIndex:indexPath.section];
            for (GGCategory *item in categoryList){
                if ([item.name isEqualToString:demand.categoryName]){
                    imagePath = item.imagePath;
                    imagePath = [imagePath stringByAppendingString:@".png"];
                    break;
                }
            }
            
            [tmp setupCell:demand AndImagePath:imagePath];
        }
        else{
            Demand *demand = [Demand new];
            [tmp setupCell:demand AndImagePath:@"shopping"];
        }
        
        cell = tmp;
    }
    
    //去掉没有内容的tableview cell的分割线
    [tableView setTableFooterView:[[UIView alloc]initWithFrame:CGRectZero]];
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (_isTrip){
        TripTableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        TripDetailTableViewController *vc = [[TripDetailTableViewController alloc]init];
        vc.tripId = cell.Id;
        vc.fromWhere = 0;
        [self.navigationController pushViewController:vc animated:YES];
        
    }
    else{
        DemandTableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        DemandDetailTableViewController *vc = [[DemandDetailTableViewController alloc]init];
        vc.demandId = cell.Id;
        vc.fromWhere = 0;
        [self.navigationController pushViewController:vc animated:YES];
    }
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
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

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 5.0;
}

/*
 originList Request -- 商品原产地查询
 */
- (void)originListRequest:(NSString *)languageCode{
    
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeNameArray = [NSArray arrayWithObjects:
                              NSLocalizedString(@"原产地", nil),
                              NSLocalizedString(@"美国", nil),
                              NSLocalizedString(@"中国", nil),
                              NSLocalizedString(@"印度", nil),
                              NSLocalizedString(@"俄罗斯", nil),
                              NSLocalizedString(@"法国", nil),
                              NSLocalizedString(@"西班牙", nil),
                              nil];
    for (NSString *item in fakeNameArray){
        GGCity *city = [GGCity new];
        city.name =item;
        [originList addObject:city];
    }
    [originList removeObjectAtIndex:0];
    [_filterMenu reloadData];
}

/*
 destinationList Request -- 商品收获地查询
 */
- (void)destinationListRequest:(NSString *)languageCode{
    
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeNameArray = [NSArray arrayWithObjects:
                              NSLocalizedString(@"收货地", nil),
                              NSLocalizedString(@"美国", nil),
                              NSLocalizedString(@"中国", nil),
                              NSLocalizedString(@"印度", nil),
                              NSLocalizedString(@"俄罗斯", nil),
                              NSLocalizedString(@"法国", nil),
                              NSLocalizedString(@"西班牙", nil),
                              nil];
    for (NSString *item in fakeNameArray){
        GGCity *city = [GGCity new];
        city.name =item;
        [destinationList addObject:city];
    }
    [destinationList removeObjectAtIndex:0];
    [_filterMenu reloadData];
}

/*
 tripOriginList Request -- 行程出发地查询
 */
- (void)tripOriginListRequest:(NSString *)languageCode{
    
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeNameArray = [NSArray arrayWithObjects:
                              NSLocalizedString(@"出发地", nil),
                              NSLocalizedString(@"美国", nil),
                              NSLocalizedString(@"中国", nil),
                              NSLocalizedString(@"印度", nil),
                              NSLocalizedString(@"俄罗斯", nil),
                              NSLocalizedString(@"法国", nil),
                              NSLocalizedString(@"西班牙", nil),
                              nil];    for (NSString *item in fakeNameArray){
        GGCity *city = [GGCity new];
        city.name = item;
        [tripOriginList addObject:city];
    }
    [tripOriginList removeObjectAtIndex:0];
    [_filterMenu reloadData];
}

/*
 tripDestinationList Request -- 行程目的地查询
 */
- (void)tripDestinationListRequest:(NSString *)languageCode{
    
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeNameArray = [NSArray arrayWithObjects:
                              NSLocalizedString(@"目的地", nil),
                              NSLocalizedString(@"美国", nil),
                              NSLocalizedString(@"中国", nil),
                              NSLocalizedString(@"印度", nil),
                              NSLocalizedString(@"俄罗斯", nil),
                              NSLocalizedString(@"法国", nil),
                              NSLocalizedString(@"西班牙", nil),
                              nil];
    for (NSString *item in fakeNameArray){
        GGCity *city = [GGCity new];
        city.name = item;
        [tripDestinationList addObject:city];
    }
    [tripDestinationList removeObjectAtIndex:0];
    [_filterMenu reloadData];
}

/*
 arrivalTimeList Request -- 到达时间查询
 */
- (void)arrivalTimeListRequest:(NSString *)languageCode{
    
    /*
     RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[GGCategory class]];
     
     
     [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
     @"name" : @"name",
     @"imagePath" : @"imagePath"
     //@"languageCode" : @"languageCode",
     //@"image" : @"image",
     //@"fileOutput" : @"fileOutput",
     //@"username" : @"username"
     }];
     
     NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
     RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:nil keyPath:nil statusCodes:statusCodes];
     
     NSString *requestString = [NSString stringWithFormat:@"%@%@%@",REST_SERVICES_URL,@"/gogou-web/services/category/list?languageCode=",languageCode];
     
     NSLog(@"%@",requestString);
     
     NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:requestString]];
     RKObjectRequestOperation *operation = [[RKObjectRequestOperation alloc] initWithRequest:request responseDescriptors:@[responseDescriptor]];
     [operation setCompletionBlockWithSuccess:^(RKObjectRequestOperation *operation, RKMappingResult *result) {
     NSArray *objects = [result array];
     categoryList = [objects mutableCopy];
     for (GGCategory *item in categoryList)
     {
     NSLog(@"%@",item.name);
     }
     }
     
     failure:^(RKObjectRequestOperation *operation, NSError *error) {
     NSLog(@"Failed with error: %@", [error localizedDescription]);
     }];
     [operation start];
     */
    
    //fake data
    NSArray *fakeTimeArray = [NSArray arrayWithObjects:
                              NSLocalizedString(@"到达时间", nil),
                              NSLocalizedString(@"今天", nil),
                              NSLocalizedString(@"明天", nil),
                              nil];
    for (NSString *item in fakeTimeArray){
        GGTime *time = [GGTime new];
        time.name = item;
        [arrivalTimeList addObject:time];
    }
    [arrivalTimeList removeObjectAtIndex:0];
    [_filterMenu reloadData];
}



/*****
 ***** tripListRequest
 *****/
- (void)tripListRequest:(TripFilter *)tripFilter{
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Trip class]];
    
    
    [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
                                                          @"arrival" : @"arrival",
                                                          @"departure" : @"departure",
                                                          @"destination" : @"destination",
                                                          @"origin" : @"origin",
                                                          @"maxheight" : @"maxheight",
                                                          @"maxlength" : @"maxlength",
                                                          @"maxweight" : @"maxweight",
                                                          @"maxwidth" : @"maxwidth",
                                                          @"userName" : @"userName",
                                                          @"description": @"descriptions",
                                                          @"categoryNames" : @"categoryNames",
                                                          @"languageCode" : @"languageCode",
                                                          @"fileOutputs" : @"fileOutputs"}];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:@"/gogou-web/services/trip/list" keyPath:nil statusCodes:statusCodes];
    
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    
    [requestMapping addAttributeMappingsFromDictionary:@{@"userName" : @"userName",
                                                         @"currentPage" : @"currentPage",
                                                         @"pageSize" : @"pageSize",
                                                         @"departure" : @"departure",
                                                         @"ratingOrder" : @"ratingOrder",
                                                         @"categoryName" : @"categoryName",
                                                         @"destination" : @"destination",
                                                         @"origin" : @"origin",
                                                         }];
    
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[TripFilter class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    
    NSLog([manager requestSerializationMIMEType]);
    
    
    
    
    // POST to create
    
    [manager postObject:tripFilter path:@"/gogou-web/services/trip/list" parameters:nil
                success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                    
                    NSArray *objects = [mappingResult array];
                    //重新加载数据
                    if (_isRefresh){
                        tripList = [objects mutableCopy];
                    }
                    //下来加载更多数据
                    else{
                        [tripList addObjectsFromArray:objects];
                    }
                    [self.tableView.mj_header endRefreshing];
                    [self.tableView.mj_footer endRefreshing];
                    [self.tableView reloadData];
                }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                    
                }
     ];
    
}


/*****
 ***** demandListRequest
 *****/
- (void)demandListRequest:(DemandFilter *)demandFilter{
    
    RKObjectMapping *responseMapping = [RKObjectMapping mappingForClass:[Demand class]];
    
    [responseMapping addAttributeMappingsFromDictionary:@{@"id" : @"Id",
                                                          @"expectTime" : @"expectTime",
                                                          @"productname" : @"productname",
                                                          @"userName" : @"userName",
                                                          @"quantity" : @"quantity",
                                                          @"brand" : @"brand",
                                                          @"serviceFee" : @"serviceFee",
                                                          @"destination" : @"destination",
                                                          @"origin" : @"origin",
                                                          @"categoryName" : @"categoryName",
                                                          @"description": @"descriptions",
                                                          @"languageCode" : @"languageCode",
                                                          @"fileOutputs" : @"fileOutputs"}];
    
    NSIndexSet *statusCodes = RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful); // Anything in 2xx
    RKResponseDescriptor *responseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:responseMapping method:RKRequestMethodAny pathPattern:@"/gogou-web/services/demand/list" keyPath:nil statusCodes:statusCodes];
    
    
    RKObjectMapping *requestMapping = [RKObjectMapping requestMapping]; // objectClass == NSMutableDictionary
    
    
    [requestMapping addAttributeMappingsFromDictionary:@{@"userName" : @"userName",
                                                         @"currentPage" : @"currentPage",
                                                         @"pageSize" : @"pageSize",
                                                         @"serviceFeeOrder" : @"serviceFeeOrder",
                                                         @"ratingOrder" : @"ratingOrder",
                                                         @"categoryName" : @"categoryName",
                                                         @"origin" : @"origin",
                                                         @"destination" : @"destination"
                                                         }];
    
    
    
    RKRequestDescriptor *requestDescriptor = [RKRequestDescriptor requestDescriptorWithMapping:requestMapping objectClass:[DemandFilter class] rootKeyPath:nil method:RKRequestMethodAny];
    
    
    RKObjectManager *manager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:REST_SERVICES_URL]];
    [manager addRequestDescriptor:requestDescriptor];
    [manager addResponseDescriptor:responseDescriptor];
    manager.requestSerializationMIMEType = RKMIMETypeJSON;
    
    
    NSLog([manager requestSerializationMIMEType]);
    
    
    // POST to create
    
    [manager postObject:demandFilter path:@"/gogou-web/services/demand/list" parameters:nil
                success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                    
                    NSArray *objects = [mappingResult array];
                    //重新加载数据
                    if (_isRefresh){
                        demandList = [objects mutableCopy];
                    }
                    //下来加载更多数据
                    else{
                        [demandList addObjectsFromArray:objects];
                    }
                    [self.tableView.mj_header endRefreshing];
                    [self.tableView.mj_footer endRefreshing];
                    [self.tableView reloadData];
                }
     
                failure:^(RKObjectRequestOperation *operation, NSError *error) {
                    
                    
                }
     ];
    
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

#pragma mark -RESTRequestListener

- (void)onGogouRESTRequestSuccess:(id)result
{
    [[CacheManager sharedManager] updateCategoryListToLocalStorage:result];
    [categoryList addObjectsFromArray:result];
    [_filterMenu reloadData];
}

- (void)onGogouRESTRequestFailure
{
}


@end
