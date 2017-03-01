//
//  OrderDetailTableViewController.m
//  Gogou
//
//  Created by xijunli on 16/9/21.
//  Copyright © 2016年 GoGou Inc. All rights reserved.
//

#import "OrderDetailTableViewController.h"
#import "OrderDetailTableViewCell0.h"
#import "OrderDetailTableViewCell1.h"
#import "TripTableViewCell.h"
#import "OrderDescription.h"
#define BACKGROUND_CORLOR [UIColor colorWithRed:70.0f/255 green:130.0f/255 blue:180.0f/255 alpha:1]

@interface OrderDetailTableViewController ()
{
    NSMutableArray *cellIdentifiers;
    OrderDescription *orderDescription;
    
}

@end

@implementation OrderDetailTableViewController

@synthesize order;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self tableViewInit];
    
    [self setupNavigationBar];
}

- (void) dataInit{
    orderDescription = (OrderDescription *) order.orderDescriptions[0];
}

- (void) cellIdentifiersInit{
    cellIdentifiers = [[NSMutableArray alloc]init];
    for (int i=0;i<2;i++)
    {
        NSString *tmp = @"OrderDetailTableViewCell";
        tmp = [tmp stringByAppendingString:[NSString stringWithFormat:@"%d",i]];
        [cellIdentifiers addObject:tmp];
    }
    [cellIdentifiers addObject:@"TripTableViewCell"];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setupNavigationBar{
    self.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    self.navigationItem.title = NSLocalizedString(@"订单详情", nil);
}

#pragma mark - Table view data source

- (void)tableViewInit{
    self.tableView.scrollEnabled = false;
    
    self.tableView.separatorColor = BACKGROUND_CORLOR;
    
    [self cellIdentifiersInit];
    
    [self dataInit];
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
   
    UITableViewCell *cell;
    
    
    if (indexPath.section == 0 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[0] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[0]];
        OrderDetailTableViewCell0 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[0] forIndexPath:indexPath];
        tmp.receiverValueLabel.text = order.username;
        
        tmp.phoneValueLabel.text = order.address.telephone;
        tmp.addressValueLabel.text = order.address.streetAddress;
        
        cell = tmp;
    }
    
    if (indexPath.section == 2 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[1] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[1]];
        OrderDetailTableViewCell1 *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[1] forIndexPath:indexPath];
        
        tmp.orderNumberKeyLabel.text = @"订单编号";
        tmp.orderNumberValueLabel.text = order.id;
        tmp.productNameValueLabel.text = orderDescription.productName;
        tmp.brandNameValueLabel.text = orderDescription.brand;
        tmp.orderDescriptionTextView.text = orderDescription.descriptions;
        tmp.categoryValueLabel.text = orderDescription.categoryName;
        tmp.quantityValueLabel.text  = [NSString stringWithFormat:@"%@",orderDescription.quantity];
        tmp.minPriceLabel.text = [NSString stringWithFormat:@"%@",orderDescription.minPrice];
        tmp.maxPriceLabel.text = [NSString stringWithFormat:@"%@",orderDescription.maxPrice];
        cell = tmp;
    }
    
    if (indexPath.section == 1 && indexPath.row == 0){
        UINib *nib = [UINib nibWithNibName:cellIdentifiers[2] bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellIdentifiers[2]];
        TripTableViewCell *tmp = [tableView dequeueReusableCellWithIdentifier:cellIdentifiers[2] forIndexPath:indexPath];
        [tmp.arrowImageView setHidden:YES];
        cell = tmp;
    }
    
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    CGFloat height = 44.0f;
    if (indexPath.section == 0 && indexPath.row == 0)
        height = 102.0f;
    if (indexPath.section == 1 && indexPath.row == 0)
        height = 60.0f;
    if (indexPath.section == 2 && indexPath.row == 0)
        height = 260.0f;
    
    return height;
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
