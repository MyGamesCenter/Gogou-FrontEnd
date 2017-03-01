//
//  ProfileTableViewController.m
//  Gogou
//
//  Created by xijunli on 16/5/26.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "ProfileTableViewController.h"
#import "ProfileTableViewCell.h"
#import "ProfileTableViewCell1.h"
#import "ProfileTableViewCell2.h"
#import "MyAddressViewController.h"
#import "Subscriber.h"
#import <CZPicker.h>
#import <RestKit/RestKit.h>
#import "CacheManager.h"
#import "RESTRequestUtils.h"
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

@interface ProfileTableViewController ()<CZPickerViewDataSource, CZPickerViewDelegate>
{
    UIButton *genderButton;
}
@property (nonatomic) Subscriber* subscriber;//登录者信息
@property (nonatomic) NSArray* genederArray;//性别数组

@end

@implementation ProfileTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self tableViewInit];
    
    [self dataInit];
    
    [self setupNavigationBar];
}

- (void)tableViewInit{
    self.tableView.scrollEnabled = false;
    
    self.tableView.separatorColor = BACKGROUND_CORLOR;
    
}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    self.navigationItem.title = NSLocalizedString(@"个人资料", nil);
}

- (void) dataInit{
    _genederArray = [NSArray arrayWithObjects:@"男", @"女", nil];
    _subscriber = [[CacheManager sharedManager] readSubscriberInfoFromLocalStorage];
}

- (NSArray *) genderArray{
    if (!_genederArray){
        _genederArray = [NSArray arrayWithObjects:@"男", @"女", nil];
    }
    return _genederArray;
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



- (IBAction)showWithGenderSelection {
    CZPickerView *picker = [[CZPickerView alloc] initWithHeaderTitle:NSLocalizedString(@"选择性别", nil) cancelButtonTitle:NSLocalizedString(@"取消", nil) confirmButtonTitle:NSLocalizedString(@"确认", nil)];
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
    NSString *string = [_genederArray objectAtIndex:row];
    NSAttributedString *att = [[NSAttributedString alloc]
                               initWithString:string
                               attributes:@{
                                            NSFontAttributeName:[UIFont fontWithName:@"Avenir-Light" size:18.0]
                                            }];
    return att;
}

- (NSString *)czpickerView:(CZPickerView *)pickerView
               titleForRow:(NSInteger)row{
    NSString *string = [_genederArray objectAtIndex:row];
    return string;
}


- (NSInteger)numberOfRowsInPickerView:(CZPickerView *)pickerView{
    return _genederArray.count;
}

- (void)czpickerView:(CZPickerView *)pickerView didConfirmWithItemAtRow:(NSInteger)row{
    NSString *string = [_genederArray objectAtIndex:row];
    if (genderButton.titleLabel.text!=string){
        [genderButton setTitle:string forState:UIControlStateNormal];
        if ([string isEqualToString:@"女"])
            _subscriber.gender = @"F";
        else
            _subscriber.gender = @"M";
        //性别信息发生改变，触发信息提交
        [RESTRequestUtils performUpdateSubsriberRequest:_subscriber delegate:self];
    }
    
}


- (void)czpickerViewDidClickCancelButton:(CZPickerView *)pickerView{
    NSLog(@"Canceled.");
}




#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return 4;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 54;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell;
    static NSString *cellIdentifier = @"ProfileTableViewCell";
    static NSString *cellIdentifier1 = @"ProfileTableViewCell1";
    static NSString *cellIdentifier2 = @"ProfileTableViewCell2";
    
    if (indexPath.row == 0)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
        
        ProfileTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        
        tmp.label.text = NSLocalizedString(@"头像", nil);
        tmp.protrait.image = [UIImage imageNamed:@"user.png"];
        cell = tmp;
    }
    
    if (indexPath.row == 1)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier1 bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier1];
        
        ProfileTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier1];
        
        tmp.keyLabel.text = NSLocalizedString(@"用户名", nil);
        if (_subscriber.userName)
            tmp.valueLabel.text = _subscriber.userName;
        tmp.arrowImg.hidden = YES;
        cell = tmp;
    }
    
    if (indexPath.row == 2)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier2 bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier2];
        
        ProfileTableViewCell2 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier2];
        tmp.keyLabel.text = NSLocalizedString(@"性别", nil);
        
        if (!genderButton){
            
            genderButton = tmp.valueButton;
            if ([_subscriber.gender isEqualToString:@"M"])
                [genderButton setTitle:@"男" forState:UIControlStateNormal];
            else
                [genderButton setTitle:@"女" forState:UIControlStateNormal];

        }
        
        cell = tmp;
    }
    
    if (indexPath.row == 3)
    {
        UINib *nib = [UINib nibWithNibName:cellIdentifier1 bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier1];
        
        ProfileTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifier1];
        
        tmp.keyLabel.text = NSLocalizedString(@"我的地址", nil);
        tmp.valueLabel.text = @"";
        cell = tmp;
    }
    
    //去掉没有内容的tableview cell的分割线
    [tableView setTableFooterView:[[UIView alloc]initWithFrame:CGRectZero]];
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (indexPath.row == 3){
        MyAddressViewController *vc = [MyAddressViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    }
    
    if (indexPath.row == 2){
        [self showWithGenderSelection];
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


#pragma mark - RESTRequestListener
- (void)onGogouRESTRequestSuccess:(id)result
{
    //alertClickFlag = true;
    
    UIAlertView *mBoxView =[[UIAlertView alloc]
                            initWithTitle:NSLocalizedString(@"修改用户信息成功！", nil)
                            message:nil
                            delegate:self
                            cancelButtonTitle:nil
                            otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
}

- (void)onGogouRESTRequestFailure
{
    //alertClickFlag = false;
    UIAlertView *mBoxView = [[UIAlertView alloc]
                             initWithTitle:NSLocalizedString(@"修改用户信息失败！", nil)
                             message:nil
                             delegate:self
                             cancelButtonTitle:nil
                             otherButtonTitles:NSLocalizedString(@"确定", nil), nil];
    [mBoxView show];
}

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
