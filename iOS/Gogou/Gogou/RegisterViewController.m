//
//  RegisterViewController.m
//  Gogou
//
//  Created by xijunli on 16/6/4.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "RegisterViewController.h"
#import "FlatButton.h"
#import "RegisterTableViewCell0.h"
#import "RegisterTableViewCell1.h"
#import "ImageProcess.h"
#import <RestKit/RestKit.h>
#import "GenericResponse.h"
//#import "GoGouConstant.h"
//#import "GoGou-Swift.h"
#import "DataProcess.h"
#import "Subscriber.h"
#import "RESTRequestUtils.h"
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

@interface RegisterViewController ()<UITableViewDataSource,UITableViewDelegate>{
    BOOL regCodeRequestFlag;
}
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UILabel *DaigouzheFlagLabel;
@property (weak, nonatomic) IBOutlet UISwitch *FlagSwitch;
@property (weak, nonatomic) IBOutlet FlatButton *registerButton;
@property (weak, nonatomic) UIButton *checkCodeButton;
@property (weak, nonatomic) UITextField *mailAddressTextField;
@property (weak, nonatomic) UITextField *accountTextField;
@property (weak, nonatomic) UITextField *checkCodeTextField;
@property (weak, nonatomic) UITextField *passwordTextField;
@property (weak, nonatomic) UITextField *passwordCheckTextField;

@end

@implementation RegisterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setupTableView];
    
    [self setupNavigationBar];
    
    [self setupRegisterButton];

}

- (void) setupTableView{
    _tableView.separatorColor = BACKGROUND_CORLOR;
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.scrollEnabled = false;
    _tableView.backgroundColor = [UIColor clearColor];
}

- (void) setupRegisterButton{
    [_registerButton setBackgroundImage:[ImageProcess imageWithColorToButton:BACKGROUND_CORLOR] forState:UIControlStateNormal];
    [_registerButton setBackgroundImage:[ImageProcess imageWithColorToButton:[UIColor colorWithRed:51.0f/255 green:112.0f/255 blue:173.0f/255 alpha:1]] forState:UIControlStateHighlighted];
    
    [_registerButton setTitle:NSLocalizedString(@"注册用户", nil) forState:UIControlStateNormal];
    [_registerButton.layer setCornerRadius:0.3];
    [_registerButton addTarget:self action:@selector(createSubscriber:) forControlEvents:UIControlEventTouchUpInside];

    [_DaigouzheFlagLabel setText:NSLocalizedString(@"我是代购人", nil)];
}

- (void) setupNavigationBar{
    
    self.navigationItem.title = NSLocalizedString(@"注册用户", nil);
    
    self.navigationController.navigationBar.tintColor = [UIColor whiteColor];
    
    UIImage *leftButtonIcon = [[UIImage imageNamed:@"backward_32.png"]
                               imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIBarButtonItem *leftButton = [[UIBarButtonItem alloc] initWithImage:leftButtonIcon
                                                                   style:UIBarButtonItemStylePlain
                                                                  target:self
                                                                  action:@selector(leftButtonClick:)];
    //self.navigationController.navigationItem.leftBarButtonItem = leftButton;
    self.navigationItem.leftBarButtonItem = leftButton;
    
}

/****
 **** 左返回按键点击事件
 ****/
- (void)leftButtonClick:(id)sender{
    [self.navigationController popViewControllerAnimated:NO];
}


-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 1;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 54;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 5;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    static NSString *cellIdentifier0 = @"RegisterTableViewCell0";
    static NSString *cellIdentifier1 = @"RegisterTableViewCell1";
    
    if (indexPath.row == 0)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier0 bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier0];
        
        RegisterTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier0 forIndexPath:indexPath];
        
        tmp.keyLabel.text = NSLocalizedString(@"邮箱", nil);
        
        if (!_mailAddressTextField){
            _mailAddressTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"请输入邮箱地址", nil);
            tmp.valueTextField.clearButtonMode = UITextFieldViewModeWhileEditing;
        }
        
        cell = tmp;
    }
    
    if (indexPath.row == 1)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier0 bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier0];
        
        RegisterTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier0 forIndexPath:indexPath];
        
        tmp.keyLabel.text = NSLocalizedString(@"用户名", nil);
        
        if (!_accountTextField){
            _accountTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"请输入用户名", nil);
            tmp.valueTextField.clearButtonMode = UITextFieldViewModeWhileEditing;
        }
        
        cell = tmp;
    }
    
    if (indexPath.row == 2)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier1 bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier1];
        
        RegisterTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier1 forIndexPath:indexPath];
        
        tmp.keyLabel.text = NSLocalizedString(@"校验码", nil);
        
        if (!_checkCodeTextField){
            _checkCodeTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"请输入校验码", nil);
            tmp.valueTextField.clearButtonMode = UITextFieldViewModeWhileEditing;
        }
        
        if (!_checkCodeButton){
            _checkCodeButton = tmp.ckCodeButton;
            [_checkCodeButton addTarget:self action:@selector(getRegCode:) forControlEvents:UIControlEventTouchUpInside];
            [_checkCodeButton setTitle:NSLocalizedString(@"| 获取校验码", nil) forState:UIControlStateNormal];
        }
        
        cell = tmp;
    }
    
    if (indexPath.row == 3)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier0 bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier0];
        
        RegisterTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier0 forIndexPath:indexPath];
        
        tmp.keyLabel.text = NSLocalizedString(@"密码", nil);
        
        if (!_passwordTextField){
            _passwordTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"请输入密码", nil);
            tmp.valueTextField.secureTextEntry = YES;
            tmp.valueTextField.clearButtonMode = UITextFieldViewModeWhileEditing;
            
        }
        
        cell = tmp;
    }
    
    if (indexPath.row == 4)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier0 bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier0];
        
        RegisterTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier0 forIndexPath:indexPath];
        
        tmp.keyLabel.text = NSLocalizedString(@"确认密码", nil);
        
        if (!_passwordCheckTextField){
            _passwordCheckTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"请再次输入密码", nil);
            tmp.valueTextField.secureTextEntry = YES;
            tmp.valueTextField.clearButtonMode = UITextFieldViewModeWhileEditing;
            
        }
        
        cell = tmp;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
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

- (void) getRegCode:(id)sender{
    
    NSString *username = [_accountTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    NSString *email = [_mailAddressTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    
    //表示是校验码按钮被点击
    regCodeRequestFlag = true;
    
    [RESTRequestUtils performGetRegCodeRequest:username
                                  emailAddress:email
                                      delegate:self];
}

- (void) createSubscriber:(id)sender{
    
    Subscriber *subscriber = [Subscriber new];
    subscriber.userName = [_accountTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    subscriber.emailAddress = [_mailAddressTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    subscriber.regCode = [_checkCodeTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    
    //NSData *data = [_passwordTextField.text dataUsingEncoding:NSASCIIStringEncoding];
    //NSData *ciphertext = [[[RNEncryptor alloc] initWithPassword:SEED_VALUE] encryptData:data];
    subscriber.encodedPassword = [DataProcess cipherWithString:_passwordTextField.text];
    
    //TODO(lxu) check password consistency
    //TODO(lxu) fcm token
    
    //表示是注册按钮被点击
    regCodeRequestFlag = false;
    
    [RESTRequestUtils performCreateSubscriberRequest:subscriber
                                            delegate:self];
}


- (BOOL)isPureInt:(NSString*)string{
    //int验证，验证数字是否为int格式
    NSScanner* scan = [NSScanner scannerWithString:string];
    int val;
    return[scan scanInt:&val] && [scan isAtEnd];
}

- (BOOL)isPureFloat:(NSString*)string{
    //float验证，验证数字是否我float格式
    NSScanner* scan = [NSScanner scannerWithString:string];
    float val;
    return[scan scanFloat:&val] && [scan isAtEnd];
}

-(BOOL)isValidateEmail:(NSString *)email{
    //验证字符串是否符合：邮箱格式
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES%@",emailRegex];
    return [emailTest evaluateWithObject:email];
}

- (void)viewWillAppear:(BOOL)animated{
    
}

- (void)viewWillDisappear:(BOOL)animated{
    
}

- (BOOL) textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - RESTRequestListener

- (void)onGogouRESTRequestSuccess:(id)result
{
    if (regCodeRequestFlag){
        UIAlertView *mBoxView =[[UIAlertView alloc]
                                initWithTitle:@"获取校验码成功"
                                message:@"请到您的邮箱检查您的校验码"
                                delegate:self
                                cancelButtonTitle:nil
                                otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
        [mBoxView show];
    }
    else{
        UIAlertView *mBoxView =[[UIAlertView alloc]
                                initWithTitle:NSLocalizedString(@"注册成功", nil)
                                message:nil
                                delegate:self
                                cancelButtonTitle:nil
                                otherButtonTitles:NSLocalizedString(@"确认", nil), nil];
        [mBoxView show];
        [self.navigationController popViewControllerAnimated:YES];
    }
    
}

- (void)onGogouRESTRequestFailure
{
    if (regCodeRequestFlag){
        UIAlertView *mBoxView =[[UIAlertView alloc]
                                initWithTitle:@"获取校验码失败"
                                message:nil
                                delegate:self
                                cancelButtonTitle:nil
                                otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
        [mBoxView show];
    }
    else{
        UIAlertView *mBoxView = [[UIAlertView alloc]
                                 initWithTitle:NSLocalizedString(@"注册失败", nil)
                                 message:nil
                                 delegate:self
                                 cancelButtonTitle:nil
                                 otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
        [mBoxView show];
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
