//
//  PostRequirementTableViewController.m
//  worldshopping2.0
//
//  Created by xijunli on 16/1/3.
//  Copyright © 2016年 SJTU. All rights reserved.
//

#define ScreenRect [UIScreen mainScreen].applicationFrame
#define ScreenRectHeight [UIScreen mainScreen].applicationFrame.size.height
#define ScreenRectWidth [UIScreen mainScreen].applicationFrame.size.width
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

#import "PostRequirementTableViewController.h"
#import "PostTableViewCell0.h"
#import "PostTableViewCell1.h"
#import "PostTableViewCell2.h"
#import "PostTableViewCell3.h"
#import "PostTableViewCell4.h"
#import "FlatButton.h"
#import "HZQDatePickerView.h"
#import "LGPhoto.h"
#import <RestKit/RestKit.h>
#import "Demand.h"
#import "GenericResponse.h"
#import "GoGouConstant.h"
#import "DateTranslation.h"
#import "GGCategory.h"
#import <CZPicker.h>
#import "MBLoadingIndicator.h"
#import "RESTRequestUtils.h"
#import "Subscriber.h"
#import "CacheManager.h"

#import "RequirementPreviewTableViewController.h"

@interface PostRequirementTableViewController ()<HZQDatePickerViewDelegate,LGPhotoPickerViewControllerDelegate,LGPhotoPickerBrowserViewControllerDataSource,LGPhotoPickerBrowserViewControllerDelegate,HZQDatePickerViewDelegate,UIActionSheetDelegate,CZPickerViewDataSource, CZPickerViewDelegate>{
    NSMutableArray *cellIdentifiers;
    FlatButton *uploadPhotoButton;
    FlatButton *submitButton;
    UIButton *precedenceButton;    //优先物品选择
    
    UIButton *expectTimeButton;
    UITextField *productnameTextField;
    
    UITextField *quantityTextField;
    UITextField *brandTextField;
    UITextField *serviceFeeTextField;
    UITextField *destinationTextField;
    UITextField *originTextField;
    UITextField *categoryNameTextField;
    UITextView *descriptionTextView;
    Subscriber *subscriber;
    HZQDatePickerView *_pikerView;
    
    
    NSMutableArray *thumbImageArray;
    NSMutableArray *originImageArray;
    NSMutableArray *imgBtnArray;
    NSMutableArray *clsBtnArray;
    
    NSString *selectedCategory;
    
}

@property (nonatomic, assign) LGShowImageType showType;
@property (nonatomic, strong)NSMutableArray *LGPhotoPickerBrowserPhotoArray;
@property (nonatomic, strong)NSMutableArray *LGPhotoPickerBrowserURLArray;
@property (nonatomic, strong) MBLoadingIndicator *loadview;

@end

@implementation PostRequirementTableViewController

@synthesize categoryList;
@synthesize userName;
@synthesize subscriberId;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self loadViewInit];
    
    [self dataInit];
    
    [self cellIdentifiersInit];
    
    [self arrayInit];
    
    [self viewInit];
    
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
    [_loadview offsetCenterYBy:300.0f];
    [_loadview setBackColor:[UIColor whiteColor]];
    [_loadview setAnimationSpeed:MBLoaderSpeedFast];
    
    //Start the loader
    //[self.loadview start];
    
    //Add the loader to our view
    [self.view addSubview:self.loadview];

}

- (void) viewInit{
    self.title = NSLocalizedString(@"发布需求", nil);

    self.tableView.allowsSelection = NO;
}

- (void) arrayInit{
    thumbImageArray = [[NSMutableArray alloc]init];
    originImageArray = [[NSMutableArray alloc]init];
    self.LGPhotoPickerBrowserPhotoArray = [[NSMutableArray alloc]init];
    imgBtnArray = [[NSMutableArray alloc]init];
    clsBtnArray = [[NSMutableArray alloc]init];
}

- (void) cellIdentifiersInit{
    cellIdentifiers = [[NSMutableArray alloc]init];
    for (int i=0;i<5;i++)
    {
        NSString *tmp = @"PostTableViewCell";
        tmp = [tmp stringByAppendingString:[NSString stringWithFormat:@"%d",i]];
        [cellIdentifiers addObject:tmp];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return 11;
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
            
            [expectTimeButton setTitle:date forState:UIControlStateNormal];
            [expectTimeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            break;
            

        default:
            break;
    }
}

- (void) setupDateViewForReceiveTime{

    [self.view endEditing:YES];
    [self setupDateView:DateTypeOfStart];
}

- (void) uploadPhotoButtonTap:(id)sender{
    
    
    UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:nil
                                                       delegate:self
                                              cancelButtonTitle:NSLocalizedString(@"取消", nil)
                                         destructiveButtonTitle:nil
                                              otherButtonTitles:NSLocalizedString(@"从相册选择", nil), NSLocalizedString(@"拍照上传", nil),   nil];
    
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
    
    thumbImageArray = [NSMutableArray array];
    originImageArray = [NSMutableArray array];
    self.LGPhotoPickerBrowserPhotoArray = [NSMutableArray array];
    
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

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    // Configure the cell...
    //注册所有可能用到的nib
    UINib *nib;
    for (int i = 0; i < 5; i++)
    {
        nib = [UINib nibWithNibName:cellIdentifiers[i] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[i]];
    }
    
    UITableViewCell *cell;
    
    if (indexPath.row == 0 )
    {
        PostTableViewCell4 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[4] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"收货时间", nil);
        
        
        if (!expectTimeButton)
        {
            expectTimeButton = tmp.ValueButton;
            [tmp.ValueButton setTitle:NSLocalizedString(@"请选择收货时间", nil) forState:UIControlStateNormal];
            [expectTimeButton addTarget:self action:@selector(setupDateViewForReceiveTime) forControlEvents:UIControlEventTouchUpInside];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 1 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"产品名", nil);
        tmp.unitLabel.text = @"";
        
        if (!productnameTextField)
        {
            productnameTextField = tmp.valueTextField;
            productnameTextField.placeholder = NSLocalizedString(@"请填写产品名", nil);
            [productnameTextField setKeyboardType:UIKeyboardTypeDefault];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 2 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"数量", nil);
        tmp.unitLabel.text = NSLocalizedString(@"/件", nil);
        
        if (!quantityTextField)
        {
            quantityTextField = tmp.valueTextField;
            quantityTextField.placeholder = NSLocalizedString(@"请填写数量", nil);
            [quantityTextField setKeyboardType:UIKeyboardTypeNumberPad];
        }
        cell = tmp;
    }
    
    
    if (indexPath.row == 3 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"品牌", nil);
        tmp.unitLabel.text = @"";
        
        if (!brandTextField)
        {
            brandTextField = tmp.valueTextField;
            brandTextField.placeholder = NSLocalizedString(@"请填写品牌", nil);
            [brandTextField setKeyboardType:UIKeyboardTypeDefault];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 4 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"服务费", nil);
        tmp.unitLabel.text = @"%";
        
        if (!serviceFeeTextField)
        {
            serviceFeeTextField = tmp.valueTextField;
            serviceFeeTextField.placeholder = NSLocalizedString(@"请填写服务费", nil);
            [serviceFeeTextField setKeyboardType:UIKeyboardTypeNumberPad];
        }
        cell = tmp;
    }
    
    
    if (indexPath.row == 5 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"收货地", nil);
        tmp.unitLabel.text = @"";
        
        if (!destinationTextField)
        {
            destinationTextField = tmp.valueTextField;
            destinationTextField.placeholder = NSLocalizedString(@"请填写目的地", nil);
            [destinationTextField setKeyboardType:UIKeyboardTypeDefault];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 6 )
    {
        PostTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"原产地", nil);
        tmp.unitLabel.text = @"";
        
        if (!originTextField)
        {
            originTextField= tmp.valueTextField;
            originTextField.placeholder = NSLocalizedString(@"请填写出发地", nil);
            [originTextField setKeyboardType:UIKeyboardTypeDefault];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 7 )
    {
        PostTableViewCell4 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[4] forIndexPath:indexPath];
        tmp.KeyLabel.text = NSLocalizedString(@"物品类型", nil);

        if (!precedenceButton)
        {
            precedenceButton = tmp.ValueButton;
            [precedenceButton setTitle:NSLocalizedString(@"请选择物品类型", nil) forState:UIControlStateNormal];
            [precedenceButton addTarget:self action:@selector(showWithMultipleSelection:) forControlEvents:UIControlEventTouchUpInside];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 8 )
    {
        PostTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        if (!descriptionTextView)
        {
            descriptionTextView = tmp.valueTextView;
            [descriptionTextView setKeyboardType:UIKeyboardTypeDefault];
        }
        cell = tmp;
    }
    
    if (indexPath.row == 9 )
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

    
    if (indexPath.row == 10 )
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
    if (indexPath.row == 9){
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

- (IBAction)showWithMultipleSelection:(id)sender {
    CZPickerView *picker = [[CZPickerView alloc] initWithHeaderTitle:NSLocalizedString(@"物品类型", nil) cancelButtonTitle:NSLocalizedString(@"取消", nil) confirmButtonTitle:NSLocalizedString(@"确认", nil)];
    picker.animationDuration = 0.92;
    picker.headerBackgroundColor = BACKGROUND_CORLOR;
    //picker.confirmButtonBackgroundColor = [UIColor colorWithRed:70.0f/255 green:138.0f/255 blue:207.0f/255 alpha:1];
    picker.delegate = self;
    picker.dataSource = self;
    //picker.allowMultipleSelection = YES;
    [picker show];
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
    if (indexPath.row == 10)
        return 55;
    if (indexPath.row == 9)
        return 285;
    if (indexPath.row == 8)
        return 191;
    return 60;
}

- (void) submitButtonClick:(id) sender{
    
    //数据检查
    int checkResult = [self checkRequData];
    if (checkResult == 1) {
        //输入数据合法
        Demand *demand = [Demand new];
        demand.expectTime = expectTimeButton.titleLabel.text;
        demand.productname = productnameTextField.text;
        demand.userName = userName;
        demand.subscriberId = subscriberId;
        demand.brand = brandTextField.text;
        demand.serviceFee = [NSNumber numberWithDouble:[serviceFeeTextField.text floatValue]/100.0];
        demand.quantity = [NSNumber numberWithInt:[quantityTextField.text intValue]];
        demand.destination = destinationTextField.text;
        demand.origin = originTextField.text;
        demand.categoryName = selectedCategory;
        demand.languageCode = @"zh-s";
        demand.descriptions = descriptionTextView.text;
        [_loadview start];
        [RESTRequestUtils performPostDemandRequest:demand
                                        imageArray:originImageArray
                                        subscriber:subscriber
                                          delegate:self];

    }
}

- (void)onGogouRESTRequestSuccess:(id)result
{
    [_loadview finish];
    
    UIAlertView *mBoxView =[[UIAlertView alloc]
                            initWithTitle:NSLocalizedString(@"需求发布成功！", nil)
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
                             initWithTitle:NSLocalizedString(@"需求发布失败！", nil)
                             message:nil
                             delegate:self
                             cancelButtonTitle:nil
                             otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
}

- (int)checkRequData{
    //判断输入值是否合法
    int checkFlag = 0;
    NSString *checkResultStr = @"";

    
    if (expectTimeButton.titleLabel.text.length == 0){
        checkFlag = 1;//预期收货时间为空
        checkResultStr = @"预期收货时间不能为空！";
    }
    else if (productnameTextField.text.length == 0){
        checkFlag = 2;//产品名为空
        checkResultStr = @"产品名不能为空！";
    }
    else if (quantityTextField.text.length == 0){
        checkFlag = 3;//数量为空
        checkResultStr = @"数量不能为空！";
    }
    else if (brandTextField.text.length == 0){
        checkFlag = 4;//品牌名为空
        checkResultStr = @"品牌名不能为空！";
    }
    else if (serviceFeeTextField.text.length == 0){
        checkFlag = 5;//服务费为空
        checkResultStr = @"服务费不能为空！";
    }
    else if (destinationTextField.text.length == 0){
        checkFlag = 6;//目的地为空
        checkResultStr = @"目的地不能为空！";
    }
    else if (originTextField.text.length == 0){
        checkFlag = 7;//出发地为空
        checkResultStr = @"出发地不能为空！";
    }
    else if (!selectedCategory){
        checkFlag = 8;//类型名为空
        checkResultStr = @"类型名不能为空！";
    }
    
    if (serviceFeeTextField.text.length !=0) {
        if (   ![self isPureInt:  serviceFeeTextField.text]
            || ![self isPureFloat:serviceFeeTextField.text])
        {
            checkFlag = 9;
            checkResultStr = @"服务费必须为纯数字！";
        }
        else{
            if ([serviceFeeTextField.text floatValue] > 100 || [serviceFeeTextField.text floatValue] < 0)
            {
                checkFlag = 10;
                checkResultStr = @"服务费必须在0% ~ 100%!";
            }
        }
    }
    
    if (quantityTextField.text.length !=0) {
        if (   ![self isPureInt:  quantityTextField.text]
            || ![self isPureFloat:quantityTextField.text])
        {
            checkFlag = 11;
            checkResultStr = @"数量必须为纯数字！";
        }
    }
    
    if (checkFlag == 0) {
        //[self addAddress];
        return 1;
    }else {
        UIAlertView *mBoxView = [[UIAlertView alloc]
                                 initWithTitle:@"发布需求"
                                 message:checkResultStr
                                 delegate:nil
                                 cancelButtonTitle:nil
                                 otherButtonTitles:@"确定", nil];
        [mBoxView show];
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
