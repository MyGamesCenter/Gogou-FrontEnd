//
//  PostTripTableViewController.m
//  worldshopping2.0
//
//  Created by xijunli on 16/1/2.
//  Copyright © 2016年 SJTU. All rights reserved.
//

// 屏幕尺寸 ScreenRect
#define ScreenRect [UIScreen mainScreen].applicationFrame
#define ScreenRectHeight [UIScreen mainScreen].applicationFrame.size.height
#define ScreenRectWidth [UIScreen mainScreen].applicationFrame.size.width

#import "PostTripTableViewController.h"
#import "PostTableViewCell0.h"
#import "PostTableViewCell1.h"
#import "PostTableViewCell2.h"
#import "PostTableViewCell3.h"
#import "PostTableViewCell4.h"
#import "PostTableViewCell5.h"
#import "FlatButton.h"
#import "TripPreviewTableViewController.h"
#import "HZQDatePickerView.h"
#import "LGPhoto.h"
#import <RestKit/RestKit.h>
#import "Trip.h"
#import "GenericResponse.h"
#import "GoGouConstant.h"
#import "DateTranslation.h"
#import "GGCategory.h"
#import <CZPicker.h>
#import "MBLoadingIndicator.h"
#import "RESTRequestUtils.h"
#import "Subscriber.h"
#import "CacheManager.h"


@interface PostTripTableViewController ()<HZQDatePickerViewDelegate,UIActionSheetDelegate,LGPhotoPickerViewControllerDelegate,LGPhotoPickerBrowserViewControllerDataSource,LGPhotoPickerBrowserViewControllerDelegate,CZPickerViewDataSource, CZPickerViewDelegate>{
    NSMutableArray *cellIdentifiers;
    FlatButton *uploadPhotoButton;
    FlatButton *submitButton;
    
    UIButton *departureTimeButton; //出发时间
    UIButton *arriveTimeButton;    //到达时间
    UIButton *precedenceButton;    //优先物品选择

    UITextField *departurePlaceTextField;//出发地
    UITextField *arrivePlaveTextField;//到达地
    UITextField *precedenceTextField;
    UITextField *maxWeightTextField;
    UITextField *maxLengthTextField;
    UITextField *maxWidthTextField;
    UITextField *maxHeightTextField;
    UITextView *descriptionTextView;
    
    
    NSMutableArray *thumbImageArray;
    NSMutableArray *originImageArray;
    NSMutableArray *imgBtnArray;
    NSMutableArray *clsBtnArray;
    
    NSString *selectedCategory;

    Subscriber *subscriber;

    HZQDatePickerView *_pikerView;
    
}

@property (nonatomic, assign)LGShowImageType showType;
@property (nonatomic, strong)NSMutableArray *LGPhotoPickerBrowserPhotoArray;
@property (nonatomic, strong)NSMutableArray *LGPhotoPickerBrowserURLArray;
@property (nonatomic, strong) MBLoadingIndicator *loadview;

@end

@implementation PostTripTableViewController

@synthesize categoryList;
@synthesize userName;
@synthesize subscriberId;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self loadViewInit];
    
    [self dataInit];

    [self cellIdentifiersInit];
    
    [self viewInit];
    
    [self arrayInit];

}

- (void) dataInit{
    subscriber = [[CacheManager sharedManager] readSubscriberInfoFromLocalStorage];
}

- (void) loadViewInit{
    //Create the loader
    _loadview = [[MBLoadingIndicator alloc] init];
    
    //NOTE: Any extra loader can be done here, including sizing, colors, animation speed, etc
    //      Pre-start changes will not be animated.
    [_loadview setLoaderStyle:MBLoaderLine];
    [_loadview setLoaderSize:MBLoaderSmall];
    [_loadview setStartPosition:MBLoaderLeft];
    [_loadview setWidth:4];
    //[_loadview setOuterLoaderBuffer:0];
    [_loadview offsetCenterYBy:400.0f];
    [_loadview setBackColor:[UIColor whiteColor]];
    [_loadview setAnimationSpeed:MBLoaderSpeedFast];
    
    //Start the loader
    //[self.loadview start];
    
    //Add the loader to our view
    [self.view addSubview:self.loadview];
}

- (void) cellIdentifiersInit{
    cellIdentifiers = [[NSMutableArray alloc]init];
    for (int i=0;i<6;i++)
    {
        NSString *tmp = @"PostTableViewCell";
        tmp = [tmp stringByAppendingString:[NSString stringWithFormat:@"%d",i]];
        [cellIdentifiers addObject:tmp];
    }
}

- (void) arrayInit{
    thumbImageArray = [[NSMutableArray alloc]init];
    originImageArray = [[NSMutableArray alloc]init];
    self.LGPhotoPickerBrowserPhotoArray = [[NSMutableArray alloc]init];
    imgBtnArray = [[NSMutableArray alloc]init];
    clsBtnArray = [[NSMutableArray alloc]init];
}

- (void) viewInit{
    self.title = NSLocalizedString(@"发布行程", nil);
    
    self.tableView.allowsSelection = NO;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)showWithMultipleSelection:(id)sender {
    CZPickerView *picker = [[CZPickerView alloc] initWithHeaderTitle:NSLocalizedString(@"物品类别", nil) cancelButtonTitle:NSLocalizedString(@"取消", nil) confirmButtonTitle:NSLocalizedString(@"确认", nil)];
    picker.animationDuration = 0.92;
    picker.headerBackgroundColor = [UIColor colorWithRed:70.0f/255 green:138.0f/255 blue:207.0f/255 alpha:1];
    //picker.confirmButtonBackgroundColor = [UIColor colorWithRed:70.0f/255 green:138.0f/255 blue:207.0f/255 alpha:1];
    picker.delegate = self;
    picker.dataSource = self;
    //picker.allowMultipleSelection = YES;
    [picker show];
}

/* comment out this method to allow
 CZPickerView:titleForRow: to work.
 */
- (NSAttributedString *)czpickerView:(CZPickerView *)pickerView
               attributedTitleForRow:(NSInteger)row{
    GGCategory *obj = [self.categoryList objectAtIndex:row];
    NSAttributedString *att = [[NSAttributedString alloc]
                               initWithString:obj.name
                               attributes:@{
                                            NSFontAttributeName:[UIFont fontWithName:@"Avenir-Light" size:18.0]
                                            }];
    return att;
}

- (NSString *)czpickerView:(CZPickerView *)pickerView
               titleForRow:(NSInteger)row{
    GGCategory *obj = [self.categoryList objectAtIndex:row];
    return obj.name;
}


- (NSInteger)numberOfRowsInPickerView:(CZPickerView *)pickerView{
    return self.categoryList.count;
}

- (void)czpickerView:(CZPickerView *)pickerView didConfirmWithItemAtRow:(NSInteger)row{
    GGCategory *obj = [self.categoryList objectAtIndex:row];
    selectedCategory = obj.name;
    [precedenceButton setTitle:obj.name forState:UIControlStateNormal];
    [precedenceButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
}


- (void)czpickerViewDidClickCancelButton:(CZPickerView *)pickerView{
    NSLog(@"Canceled.");
}


- (void) uploadPhotoButtonTap:(id)sender{
    
    
    UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:nil
                                                       delegate:self
                                              cancelButtonTitle:NSLocalizedString(@"取消", nil)
                                         destructiveButtonTitle:nil
                                              otherButtonTitles:NSLocalizedString(@"从相册选择", nil),NSLocalizedString(@"拍照上传", nil),   nil];
    
    [sheet showFromRect:self.view.frame inView:self.view animated:YES];
    
    
}

- (void)actionSheet:(UIActionSheet *)actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == actionSheet.cancelButtonIndex) {
        return;
    }
    
    switch (buttonIndex) {
        case 0:
        {
            [self presentPhotoPickerViewControllerWithStyle:LGShowImageTypeImagePicker];
        }
            break;
            
        case 1:
        {
            [self presentCameraContinuous];
        }
            break;
            
    }
}



/**
 *  初始化相册选择器
 */
- (void)presentPhotoPickerViewControllerWithStyle:(LGShowImageType)style {
    LGPhotoPickerViewController *pickerVc = [[LGPhotoPickerViewController alloc] initWithShowType:style];
    pickerVc.status = PickerViewShowStatusCameraRoll;
    pickerVc.maxCount = 6;   // 最多能选9张图片
    pickerVc.delegate = self;
    self.showType = style;
    [pickerVc showPickerVc:self];
}

/**
 *  初始化自定义相机（单拍）
 */
- (void)presentCameraSingle {
    /*
    ZLCameraViewController *cameraVC = [[ZLCameraViewController alloc] init];
    // 拍照最多个数
    cameraVC.maxCount = 6;
    // 单拍
    cameraVC.cameraType = ZLCameraSingle;
    cameraVC.callback = ^(NSArray *cameras){
        //在这里得到拍照结果
        //数组元素是ZLCamera对象
        ZLCamera *canamerPhoto = cameras[0];
        photoImageView.image = canamerPhoto.photoImage;
        originImage = canamerPhoto.photoImage;
        self.LGPhotoPickerBrowserPhotoArray = [[NSArray alloc]initWithObjects:originImage, nil];
        
    };
    [cameraVC showPickerVc:self];
    */
}

/**
 *  初始化自定义相机（连拍）
 */
- (void)presentCameraContinuous {
    ZLCameraViewController *cameraVC = [[ZLCameraViewController alloc] init];
    thumbImageArray = [NSMutableArray array];
    originImageArray = [NSMutableArray array];
    self.LGPhotoPickerBrowserPhotoArray = [NSMutableArray array];
    // 拍照最多个数
    cameraVC.maxCount = 6;
    // 连拍
    cameraVC.cameraType = ZLCameraContinuous;
    cameraVC.callback = ^(NSArray *cameras){
        //在这里得到拍照结果o
        //数组元素是ZLCamera对象
        /*
         @exemple
         ZLCamera *canamerPhoto = cameras[0];
         UIImage *image = canamerPhoto.photoImage;
         */
        for (ZLCamera *cameraPhoto in cameras){
            //UIImage *thumbImage = cameraPhoto.thumbImage;
            //UIImage *photoImage = cameraPhoto.photoImage;
            [thumbImageArray addObject:cameraPhoto.thumbImage];
            [originImageArray addObject:cameraPhoto.photoImage];
            LGPhotoPickerBrowserPhoto *photo = [[LGPhotoPickerBrowserPhoto alloc]init];
            photo.photoImage = cameraPhoto.photoImage;
            [self.LGPhotoPickerBrowserPhotoArray addObject:photo];
        }
        [self.tableView reloadData];
    };
    [cameraVC showPickerVc:self];
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
    [self.tableView reloadData];
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

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return 12;
}

- (void)setupDateView:(DateType)type {
    
    _pikerView = [HZQDatePickerView instanceDatePickerView];
    _pikerView.frame = CGRectMake(0, 0, ScreenRectWidth, ScreenRectHeight);
    [_pikerView setBackgroundColor:[UIColor clearColor]];
    _pikerView.delegate = self;
    _pikerView.type = type;
    _pikerView.datePickerView.datePickerMode = UIDatePickerModeDateAndTime;
    [_pikerView.datePickerView setMinimumDate:[NSDate date]];
    [self.view addSubview:_pikerView];
    
    
}

- (void)getSelectDate:(NSString *)date type:(DateType)type {
    
    NSDate *dateDate = [DateTranslation DateTranslatedFromString:date withLocaleIdentifier:@"en_US"];
    date = [DateTranslation StringTranslatedFromDate:dateDate withLocaleIdentifier:@"zh_CN"];
    switch (type) {
        case DateTypeOfStart:
            
            [departureTimeButton setTitle:date forState:UIControlStateNormal];
            [departureTimeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            break;
            
        case DateTypeOfEnd:
    
            [arriveTimeButton setTitle:date forState:UIControlStateNormal];
            [arriveTimeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            break;
            
        default:
            break;
    }
}

- (void) setupDateViewForDepartureTime{

    [self.view endEditing:YES];
    [self setupDateView:DateTypeOfStart];
}

- (void) setupDateViewForArriveTime{
    
    [self.view endEditing:YES];
    [self setupDateView:DateTypeOfEnd];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    // Configure the cell...
    //注册所有可能用到的nib
    
    UINib *nib;
    for (int i = 0; i < 6; i++)
    {
        nib = [UINib nibWithNibName:cellIdentifiers[i] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[i]];
    }
    
    UITableViewCell *cell;
    
    if (indexPath.row == 0 )
    {
        PostTableViewCell4 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[4] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"出发时间", nil);
        
        //表示首次加载该cell
        if (!departureTimeButton)
        {
            departureTimeButton = tmp.ValueButton;
            [tmp.ValueButton setTitle:NSLocalizedString(@"请选择出发时间", nil) forState:UIControlStateNormal];
            [departureTimeButton addTarget:self action:@selector(setupDateViewForDepartureTime) forControlEvents:UIControlEventTouchUpInside];
            
            //[departureTimeTextField setEnabled:NO];
            //[departurePlaceTextField setKeyboardType:UIKeyboardTypeDefault];
        }
        
        cell = tmp;
    }
    
    if (indexPath.row == 1 )
    {
        PostTableViewCell4 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[4] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"到达时间", nil);
        
        //表示首次加载该cell
        if (!arriveTimeButton)
        {
            arriveTimeButton = tmp.ValueButton;
            [tmp.ValueButton setTitle:NSLocalizedString(@"请选择到达时间", nil) forState:UIControlStateNormal];
            [arriveTimeButton addTarget:self action:@selector(setupDateViewForArriveTime) forControlEvents:UIControlEventTouchUpInside];
            
        }

        cell = tmp;
    }
    
    if (indexPath.row == 2 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"出发地", nil);
        tmp.unitLabel.text = @"";
        if (!departurePlaceTextField)
        {
            departurePlaceTextField = tmp.valueTextField;
            departurePlaceTextField.placeholder = NSLocalizedString(@"请填写出发地", nil);
            [departurePlaceTextField setKeyboardType:UIKeyboardTypeDefault];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 3 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"目的地", nil);
        tmp.unitLabel.text = @"";
        if (!arrivePlaveTextField)
        {
            arrivePlaveTextField = tmp.valueTextField;
            arrivePlaveTextField.placeholder = NSLocalizedString(@"请填写目的地", nil);
            [arrivePlaveTextField setKeyboardType:UIKeyboardTypeDefault];
        }
        cell = tmp;
    }
    
    
    if (indexPath.row == 4 )
    {
        PostTableViewCell4 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[4] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"有限携带物品", nil);
        /*
        if (!precedenceTextField)
        {
            precedenceTextField = tmp.valueTextField;
            precedenceTextField.placeholder = @"请填写优先携带物品";
            [precedenceTextField setKeyboardType:UIKeyboardTypeDefault];
        }*/
        if (!precedenceButton)
        {
            precedenceButton = tmp.ValueButton;
            [precedenceButton setTitle:NSLocalizedString(@"请选择有限携带物品", nil) forState:UIControlStateNormal];
            [precedenceButton addTarget:self action:@selector(showWithMultipleSelection:) forControlEvents:UIControlEventTouchUpInside];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 5 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"最大重量", nil);
        tmp.unitLabel.text = NSLocalizedString(@"/千克", nil);
        if (!maxWeightTextField)
        {
            maxWeightTextField = tmp.valueTextField;
            maxWeightTextField.placeholder = NSLocalizedString(@"请填写携带物品最大重量", nil);
            [maxWeightTextField setKeyboardType:UIKeyboardTypeNumberPad];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 6 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"最大长度", nil);
        tmp.unitLabel.text = NSLocalizedString(@"/米", nil);
        if (!maxLengthTextField)
        {
            maxLengthTextField = tmp.valueTextField;
            maxLengthTextField.placeholder = NSLocalizedString(@"请填写携带物品最大长度", nil);
            [maxLengthTextField setKeyboardType:UIKeyboardTypeNumberPad];
        }
        cell = tmp;
    }
    if (indexPath.row == 7 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"最大宽度", nil);
        tmp.unitLabel.text = NSLocalizedString(@"/米", nil);
        if (!maxWidthTextField)
        {
            maxWidthTextField = tmp.valueTextField;
            maxWidthTextField.placeholder = NSLocalizedString(@"请填写携带物品最大宽度", nil);
            [maxWidthTextField setKeyboardType:UIKeyboardTypeNumberPad];
        }
        cell = tmp;
    }
    if (indexPath.row == 8 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"最大高度", nil);
        tmp.unitLabel.text = NSLocalizedString(@"/米", nil);
        if (!maxHeightTextField)
        {
            maxHeightTextField = tmp.valueTextField;
            maxHeightTextField.placeholder = NSLocalizedString(@"请填写携带物品最大高度", nil);
            [maxHeightTextField setKeyboardType:UIKeyboardTypeNumberPad];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 9 )
    {
        PostTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        if (!descriptionTextView)
        {
            descriptionTextView = tmp.valueTextView;
            [descriptionTextView setKeyboardType:UIKeyboardTypeDefault];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 10 )
    {
        PostTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[2] forIndexPath:indexPath];
        [tmp awakeFromNib];
        if (!uploadPhotoButton)
        {
            uploadPhotoButton = tmp.button;
            [uploadPhotoButton addTarget:self action:@selector(uploadPhotoButtonTap:) forControlEvents:UIControlEventTouchUpInside];
        }
        
        for (int i=0;i<thumbImageArray.count;i++)
        {
           
            [tmp.picArray[i] setBackgroundImage:thumbImageArray[i] forState:UIControlStateNormal];
            [tmp.picArray[i] addTarget:self action:@selector(picButtonTap:) forControlEvents:UIControlEventTouchDown];
            [tmp.closeArray[i] setHidden:NO];
            [tmp.closeArray[i] addTarget:self action:@selector(deletePic:) forControlEvents:UIControlEventTouchDown];
        }

        cell = tmp;
    }
    
    if (indexPath.row == 11 )
    {
        PostTableViewCell3 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[3] forIndexPath:indexPath];
        if (!submitButton)
        {
            submitButton = tmp.submitButton;
            [submitButton addTarget:self action:@selector(submitButtonClick:) forControlEvents:UIControlEventTouchUpInside];
        }
        cell = tmp;
    }
    return cell;
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 10){
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


- (void)deletePic:(UIButton *) btn{
    //UIButton *tmp = (UIButton*)sender;
    NSLog(@"%ld",(long)btn.tag);
    [thumbImageArray removeObjectAtIndex:btn.tag];
    [_LGPhotoPickerBrowserPhotoArray removeObjectAtIndex:btn.tag];
    [self.tableView reloadData];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 11 )//提交cell高度
        return 55;
    if (indexPath.row == 10)//上传图片cell高度
        return 285;
    if (indexPath.row == 9)//详细描述cell高度
        return 191;
    return 60;
}



- (int)checkTripData{
    //判断输入值是否合法
    int checkFlag = 0;
    NSString *checkResultStr = @"";
    
    if (departurePlaceTextField.text.length == 0){
        checkFlag = 1;//出发地为空
        checkResultStr = @"出发地不能为空！";
    }
    else if (arrivePlaveTextField.text.length == 0){
        checkFlag = 2;//目的地为空
        checkResultStr = @"目的地不能为空！";
    }
    else if (!selectedCategory){
        checkFlag = 4;//优先携带为空
        checkResultStr = @"优先携带物品为空，系统为您置为“无”！";
        precedenceTextField.text = @"无";
    }
    else if (maxWeightTextField.text.length == 0){
        checkFlag = 5;//最大重量为空
        checkResultStr = @"最大重量不能为空！";
    }
    else if (maxLengthTextField.text.length == 0){
        checkFlag = 6;//最大长度为空
        checkResultStr = @"最大长度不能为空！";
    }
    else if (maxWidthTextField.text.length == 0){
        checkFlag = 7;//最大宽度为空
        checkResultStr = @"最大宽度不能为空！";
    }
    else if (maxHeightTextField.text.length == 0){
        checkFlag = 8;//最大高度为空
        checkResultStr = @"最大高度不能为空！";
    }
    else if (departureTimeButton.titleLabel.text.length == 0){
        checkFlag = 9;//出发时间为空
        checkResultStr = @"出发时间不能为空！";
    }
    else if (arriveTimeButton.titleLabel.text.length == 0){
        checkFlag = 10;//到达时间为空
        checkResultStr = @"到达时间不能为空！";
    }

    
    if (maxWeightTextField.text.length !=0) {
        if (   ![self isPureInt:  maxWeightTextField.text]
            || ![self isPureFloat:maxWeightTextField.text])
        {
            checkFlag = 11;
            checkResultStr = @"最大重量必须为纯数字！";
        }
    }
    
    if (maxLengthTextField.text.length !=0) {
        if (   ![self isPureInt:  maxLengthTextField.text]
            || ![self isPureFloat:maxLengthTextField.text])
        {
            checkFlag = 12;
            checkResultStr = @"最大长度必须为纯数字！";
        }
    }
    if (maxWidthTextField.text.length !=0) {
        if (   ![self isPureInt:  maxWidthTextField.text]
            || ![self isPureFloat:maxWidthTextField.text])
        {
            checkFlag = 13;
            checkResultStr = @"最大宽度必须为纯数字！";
        }
    }
    if (maxHeightTextField.text.length !=0) {
        if (   ![self isPureInt:  maxHeightTextField.text]
            || ![self isPureFloat:maxHeightTextField.text])
        {
            checkFlag = 14;
            checkResultStr = @"最大高度必须为纯数字！";
        }
    }
    
    if (arriveTimeButton.titleLabel.text.length != 0 && departureTimeButton.titleLabel.text.length != 0)
    {
        NSDate *arriveTime = [DateTranslation DateTranslatedFromString:arriveTimeButton.titleLabel.text withLocaleIdentifier:@"zh_CN"];
        NSDate *departureTime = [DateTranslation DateTranslatedFromString:departureTimeButton.titleLabel.text withLocaleIdentifier:@"zh_CN"];
        if ([arriveTime compare:departureTime] == NSOrderedAscending)//如果到达时间比出发时间还早
        {
            checkFlag = 15;
            checkResultStr = @"到达时间不能比出发时间早！";
        }
            
    }
    
    if (checkFlag == 0) {
        //数据合法，返回1
        return 1;
    }else {
        UIAlertView *mBoxView = [[UIAlertView alloc]
                                 initWithTitle:@"发布行程"
                                 message:checkResultStr
                                 delegate:nil
                                 cancelButtonTitle:nil
                                 otherButtonTitles:@"确定", nil];
        [mBoxView show];
        //数据不合法，返回0
        return 0;
    }
}

- (BOOL)isPureInt:(NSString*)string{
    NSScanner* scan = [NSScanner scannerWithString:string];
    int val;
    return[scan scanInt:&val] && [scan isAtEnd];
}

- (BOOL)isPureFloat:(NSString*)string{
    NSScanner* scan = [NSScanner scannerWithString:string];
    float val;
    return[scan scanFloat:&val] && [scan isAtEnd];
}

/****
 submit按钮被点击
 ****/
- (void) submitButtonClick:(id) sender{
    //数据检查
    int checkResult = [self checkTripData];
    if (checkResult == 1)//数据合法
    {
        Trip *trip = [Trip new];
        
        trip.departure = departureTimeButton.titleLabel.text;
        trip.arrival = arriveTimeButton.titleLabel.text;
        trip.userName = userName;
        trip.subscriberId = subscriberId;
        trip.maxheight = [NSNumber numberWithFloat:[maxHeightTextField.text floatValue]];
        trip.maxlength = [NSNumber numberWithFloat:[maxLengthTextField.text floatValue]];
        trip.maxweight = [NSNumber numberWithFloat:[maxWeightTextField.text floatValue]];
        trip.maxwidth = [NSNumber numberWithFloat:[maxWeightTextField.text floatValue]];
        trip.destination = arrivePlaveTextField.text;
        trip.origin = departurePlaceTextField.text;
        trip.categoryNames = [NSMutableArray arrayWithObjects:selectedCategory,nil];
        trip.languageCode = @"zh-s";
        trip.descriptions = descriptionTextView.text;
        [self.loadview start];
        
        [RESTRequestUtils performPostTripRequest:trip
                                      imageArray:originImageArray
                                        subscriber:subscriber
                                        delegate:self];

    }
}

- (void)onGogouRESTRequestSuccess:(id)result
{
    [_loadview finish];
    
    UIAlertView *mBoxView =[[UIAlertView alloc]
                            initWithTitle:NSLocalizedString(@"行程发布成功！", nil)
                            message:NSLocalizedString(@"您已回到首页", nil)
                            delegate:self
                            cancelButtonTitle:nil
                            otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)onGogouRESTRequestFailure
{
    [_loadview finish];
    UIAlertView *mBoxView = [[UIAlertView alloc]
                             initWithTitle:NSLocalizedString(@"行程发布失败！", nil)
                             message:nil
                             delegate:self
                             cancelButtonTitle:nil
                             otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
}


/*
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:<#@"reuseIdentifier"#> forIndexPath:indexPath];
 
    // Configure the cell...
 
    return cell;
}
*/

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Table view delegate

// In a xib-based application, navigation from a table can be handled in -tableView:didSelectRowAtIndexPath:
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Navigation logic may go here, for example:
    // Create the next view controller.
    <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:<#@"Nib name"#> bundle:nil];
    
    // Pass the selected object to the new view controller.
    
    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
}
*/

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
