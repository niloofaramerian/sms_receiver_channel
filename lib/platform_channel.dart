import 'package:flutter/services.dart';

class PlatformChannel {
  static const _channel = MethodChannel("sms.receiver.channel");

  Future<String> receiveSms() async {
    try {
      final result = await _channel.invokeMethod("receive_sms");
      return result as String;
    } catch (e) {
      return 'Failed to receive sms : ${e.toString()}';
    }
  }
}
