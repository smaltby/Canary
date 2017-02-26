#import <Foundation/Foundation.h>
@interface CommandParser_Wrapper : NSObject
- (void)parse_wrapped:(NSString *)command with_token:(NSString *)accessToken;
@end
