#import "NativeBridgeObjC.h"
#import "Canary-Swift.h"
#include "CommandParser.h"
@implementation NativeBridgeObjC

void NativePlayUri(const char* uri)
{
    NativeBridgeObjC* self = [[NativeBridgeObjC alloc] init];
    NSString *nsURI = [NSString stringWithUTF8String:uri];
    [(id)self playUri:nsURI];
}

void NativePause()
{
    
}

void NativeResume()
{
    
}

void NativeNext()
{
    
}

void NativeToggleShuffle(bool shuffle)
{
    
}

void NativeToggleRepeat(bool repeat)
{
    
}

- (void)playUri:(NSString *) uri
{
    [NativeFunctions playUri:uri];
}

- (void)parse_wrapped:(NSString *)command with_token:(NSString *)accessToken
{
    CommandParser parser;
    parser.parse([command cStringUsingEncoding:NSUTF8StringEncoding], [accessToken cStringUsingEncoding:NSUTF8StringEncoding]);
}

@end

