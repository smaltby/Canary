#import <Foundation/Foundation.h>
@interface NativeBridge : NSObject
- (NSString *)parse_wrapped:(NSString *)command with_token:(NSString *)accessToken;
@end
