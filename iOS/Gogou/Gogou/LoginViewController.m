//
//  LoginViewController.m
//  Gogou
//
//  Created by xijunli on 16/6/3.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "LoginViewController.h"
#import "WXApiManager.h"
#import "RegisterViewController.h"
#import "LoginInfo.h"
#import "Subscriber.h"
#import "GoGouConstant.h"
#import "FlatButton.h"
#import "ImageProcess.h"
#import "loginTableViewCell.h"
#import "LoginInfo.h"
#import "DataProcess.h"
#import "RESTRequestUtils.h"
#import "CacheManager.h"
#import "GoGouColors.h"
#import "UIView+Toast.h"
#import <RestKit/RestKit.h>
#import <RongIMKit/RongIMKit.h>


@interface LoginViewController ()<UITableViewDataSource, UITableViewDelegate, WXApiManagerDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UILabel *otherWayLoginLabel;
@property (weak, nonatomic) IBOutlet FlatButton *loginButton;
@property (weak, nonatomic) IBOutlet UIButton *wechatLoginButton;
@property (weak, nonatomic) IBOutlet UIButton *forgetPasswordButton;
@property (weak, nonatomic) IBOutlet UIButton *registerButton;
@property (weak, nonatomic) UITextField *accountTextField;
@property (weak, nonatomic) UITextField *passwordTextField;

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setupTableView];
    [self setupNavigationBar];
    [self setupLoginButton];
    [self setupRegisterButton];
    [self setupForgetPasswordButton];
    [WXApiManager sharedManager].delegate = self;
    
}

- (void) setupForgetPasswordButton{
    [_forgetPasswordButton setTitle:NSLocalizedString(@"忘记密码？", nil) forState:UIControlStateNormal];
}

- (void) setupRegisterButton{
    [_registerButton addTarget:self action:@selector(registerButtonTap:) forControlEvents:UIControlEventTouchUpInside];
    [_registerButton setTitle:NSLocalizedString(@"注册用户", nil) forState:UIControlStateNormal];
}

- (void) setupTableView{
    _tableView.separatorColor = APP_MAIN_CORLOR;
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.scrollEnabled = false;
    _tableView.backgroundColor = [UIColor clearColor];
}


- (void) setupNavigationBar{
    
    self.navigationItem.title = NSLocalizedString(@"登录", nil);
    
    self.navigationController.navigationBar.tintColor = [UIColor whiteColor];
    
    UIImage *leftButtonIcon = [[UIImage imageNamed:@"backward"]
                               imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIBarButtonItem *leftButton = [[UIBarButtonItem alloc] initWithImage:leftButtonIcon
                                                                   style:UIBarButtonItemStylePlain
                                                                  target:self
                                                                  action:@selector(leftButtonClick:)];
    self.navigationItem.leftBarButtonItem = leftButton;
    
}

- (void) setupLoginButton{
    [_loginButton setBackgroundImage:[ImageProcess imageWithColorToButton:APP_MAIN_CORLOR] forState:UIControlStateNormal];
    [_loginButton setBackgroundImage:[ImageProcess imageWithColorToButton:ROYAL_BLUE] forState:UIControlStateHighlighted];
    
    [_loginButton setTitle:NSLocalizedString(@"登录", nil) forState:UIControlStateNormal];

    [_loginButton.layer setCornerRadius:0.3];
    
    [_loginButton addTarget:self action:@selector(loginButtonTap:) forControlEvents:UIControlEventTouchUpInside];
    
    [_wechatLoginButton addTarget:self action:@selector(sendAuthRequest) forControlEvents:UIControlEventTouchUpInside];
    
    [_otherWayLoginLabel setText:NSLocalizedString(@"其他登录方式", nil)];
}


/****
 **** 左返回按键点击事件
 ****/
- (void)leftButtonClick:(id)sender{
    [self.navigationController popViewControllerAnimated:NO];
}

- (void)registerButtonTap:(id) sender{
    RegisterViewController *vc = [RegisterViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
    return 2;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    static NSString *cellIdentifier = @"loginTableViewCell";
    UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
    [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
    
    if (indexPath.row == 0)
    {
        loginTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"用户名", nil);
        
        if (!_accountTextField){
            _accountTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"用户名/邮箱", nil);
            tmp.valueTextField.clearButtonMode = UITextFieldViewModeWhileEditing;
            tmp.valueTextField.keyboardType = UIKeyboardTypeASCIICapable;
        }
        
        cell = tmp;
    }
    
    else if (indexPath.row == 1)
    {
        loginTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
        tmp.keyLabel.text = NSLocalizedString(@"密码", nil);
        
        if (!_passwordTextField){
            _passwordTextField = tmp.valueTextField;
            tmp.valueTextField.placeholder = NSLocalizedString(@"请输入密码", nil);
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

- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    return nil;
}

/****
 ****  登陆按键点击事件
 ****/
- (void) loginButtonTap:(id)sender{
    LoginInfo *loginInfo = [LoginInfo new];
    // TODO verfiy email format
    loginInfo.username = _accountTextField.text;
    loginInfo.emailAddress = @"xxxxxx";

    loginInfo.password = [DataProcess cipherWithString:_passwordTextField.text];
    [RESTRequestUtils performLoginRequestWith:loginInfo delegate:self];
}

- (void)sendAuthRequest {
    
    SendAuthReq *req = [[SendAuthReq alloc]init];
    req.scope = @"snsapi_userinfo";
    req.state = @"wechat_gogou";
    req.openID = WEIXIN_APP_ID;
    [WXApi sendAuthReq:req viewController:self delegate:[[UIApplication sharedApplication] delegate]];
}

//Implementing various delegates

- (void)onGogouRESTRequestSuccess:(id)result
{
    //希望后台不要返回字段为null的，起码也要赋上一个空值
    // TODO
    Subscriber *subscriber = result;
    if (!subscriber.userName)
        subscriber.userName = @"";
    if (!subscriber.emailAddress)
        subscriber.emailAddress =@"";
    if (!subscriber.encodedPassword)
        subscriber.encodedPassword = @"";
    if (!subscriber.clearPassword)
        subscriber.clearPassword = @"";
    if (!subscriber.isPurchaser)
        subscriber.isPurchaser = 0;
    if (!subscriber.gender)
        subscriber.gender = @"";
    if (!subscriber.firstName)
        subscriber.firstName = @"";
    if (!subscriber.lastName)
        subscriber.lastName = @"";
    if (!subscriber.gcmToken)
        subscriber.gcmToken = @"";
    if (!subscriber.imCode)
        subscriber.imCode = @"";
    if (!subscriber.imToken)
        subscriber.imToken = @"";
    if (!subscriber.age)
        subscriber.age = 0;
    
    if (subscriber)
    {
        //登录成功，记录登录信息到NSUserDefaults中
        [[CacheManager sharedManager] updateSubscriberInfoToLocalStorage:subscriber];
        
        /*
        //通过返回的token登录融云服务器
        NSString *token=subscriber.imToken;
        [[RCIM sharedRCIM] connectWithToken:token success:^(NSString *userId) {
            //设置用户信息提供者,页面展现的用户头像及昵称都会从此代理取
            [[RCIM sharedRCIM] setUserInfoDataSource:self];
            NSLog(@"Login successfully with userId: %@.", userId);
         
            // dispatch_async(dispatch_get_main_queue(), ^{
            // ChatListViewController *chatListViewController = [[ChatListViewController alloc]init];
            // [self.navigationController pushViewController:chatListViewController animated:YES];
            //  });
         
        } error:^(RCConnectErrorCode status) {
            NSLog(@"login error status: %ld.", (long)status);
        } tokenIncorrect:^{
            NSLog(@"token 无效 ，请确保生成token 使用的appkey 和初始化时的appkey 一致");
        }];*/
    }
    
    [self.view makeToast:NSLocalizedString(@"登录成功", nil)
                duration:3.0
                position:CSToastPositionBottom
                   title:nil
                   image:nil
                   style:nil
              completion:^(BOOL didTap) {
                  if (didTap)
                  {
                      [self.navigationController popViewControllerAnimated:YES];
                  }
                  else
                  {
                      [self.navigationController popViewControllerAnimated:YES];
                  }
              }];
}

- (void)onGogouRESTRequestFailure
{
    [self.view makeToast:NSLocalizedString(@"登录失败", nil)];
}

#pragma mark -WXApiManagerDelegate
- (void)managerDidRecvAuthResponse:(SendAuthResp *)response
{
    NSLog(@"code:%@,state:%@,errcode:%d", response.code, response.state, response.errCode);
    
    [RESTRequestUtils performOAuth2LoginWith:response.code delegate:self];
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

