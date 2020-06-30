import 'package:jomla/data/models/responses/data/partner_data.dart';

abstract class ProfileState {
  final PartnerData partnerData;

  ProfileState(this.partnerData);
}

class InitialProfileState extends ProfileState {
  InitialProfileState(PartnerData partnerData) : super(partnerData);
}

class ErrorUpdateProfileState extends ProfileState {
  ErrorUpdateProfileState(PartnerData partnerData) : super(partnerData);
}

class OnGotProfileState extends ProfileState {
  OnGotProfileState(PartnerData partnerData) : super(partnerData);
}

class SuccessLogoutState extends ProfileState {
  SuccessLogoutState(PartnerData partnerData) : super(partnerData);
}

class SuccessUpdateProfileState extends ProfileState {
  SuccessUpdateProfileState(PartnerData partnerData) : super(partnerData);
}

class SuccessClickDoneState extends ProfileState {
  SuccessClickDoneState(PartnerData partnerData) : super(partnerData);
}

class SuccessCreateNewPartnerState extends ProfileState {
  SuccessCreateNewPartnerState(PartnerData partnerData) : super(partnerData);
}

class SuccessUpdatePartnerState extends ProfileState {
  SuccessUpdatePartnerState(PartnerData partnerData) : super(partnerData);
}

class SuccessDeletePartnerState extends ProfileState {
  SuccessDeletePartnerState(PartnerData partnerData) : super(partnerData);
}

class SetFulNameEvent extends ProfileState {
  String name;
  final PartnerData partnerData;

  SetFulNameEvent(this.name, {this.partnerData}) : super(partnerData);
}

class SetEmailEvent extends ProfileState {
  String email;
  final PartnerData partnerData;
  SetEmailEvent(this.email, {this.partnerData}) : super(partnerData);
}

class SetPhoneNumberEvent extends ProfileState {
  String phone;
  final PartnerData partnerData;
  SetPhoneNumberEvent(this.phone, {this.partnerData}) : super(partnerData);
}

class SetUserImageEvent extends ProfileState {
  String userImage;
  final PartnerData partnerData;
  SetUserImageEvent(this.userImage, {this.partnerData}) : super(partnerData);
}

class SetCompanyNameEvent extends ProfileState {
  String companyName;
  final PartnerData partnerData;
  SetCompanyNameEvent(this.companyName, {this.partnerData}) : super(partn