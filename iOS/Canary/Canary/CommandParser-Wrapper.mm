#import "CommandParser-Wrapper.h"
#include "CommandParser.h"
@implementation CommandParser_Wrapper
- (void)parse_wrapped:(NSString *)command with_token:(NSString *)accessToken
{
    CommandParser parser;
    parser.parse([command cStringUsingEncoding:NSUTF8StringEncoding], [accessToken cStringUsingEncoding:NSUTF8StringEncoding]);
}
@end
