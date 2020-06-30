import 'package:jomla/data/models/responses/data/partner_data.dart';
import 'package:jomla/domain/blocs/base/base_bloc.dart';
import 'package:jomla/domain/blocs/base/toolbar/toolbar_event.dart';
import 'package:jomla/domain/blocs/global/global_bloc.dart';
import 'package:jomla/domain/blocs/profile/profile_event.dart';
import 'package:jomla/domain/blocs/profile/profile_state.dart';
import 'package:jomla/domain/use_cases/location/models/location_model.dart';
import 'package:jomla/domain/use_cases/profile/profile_use_case.dart';
import 'package:jomla/domain/use_cases/profile/profile_use_case_impl.dart';
import 'package:jomla/utils/image_utils.dart';
import 'package:jomla/utils/validation_utils.dart';

class ProfileBloc extends BaseBloc<dynamic, ProfileState> {
  ProfileUseCase _profileUseCase = ProfileUseCaseImpl();
  GlobalBloc globalBloc;

  String _name = '';
  String _email = '';
  String _phone = '';
  String _userImage = '';
  String _companyName = '';
  bool _isPrimary = false;

  ProfileBloc(this.globalBloc);

  @override
  ProfileState get initialState => InitialProfileState(globalBloc.partnerData);

  @override
  Stream<ProfileState> mapEventToState(dynamic event) async* {
    if (event is SuccessProfileEvent)
      yield OnGotProfileState(event.partnerData);
    if (event is DoLogoutEvent) _logout();
    if (event is SuccessLogoutEvent)
      yield SuccessLogoutState(globalBloc.partnerData);

    if (event is UpdateProfileEvent) {
      if (_validationFields()) {
        PartnerData parentData = _fillModelForUpdateProfile(event.partnerData);
        _updateUserProfile(parentData);
      } else {
        print('ErrorUpdateProfileState');
        yield ErrorUpdateProfileState(globalBloc.partnerData);
      }
    }
    if (event is SuccessUpdateProfileEvent)
      SuccessUpdateProfileState(event.partnerData);

    if (event is TBRightActionEvent)
      yield SuccessClickDoneState(globalBloc.partnerData);
    if (event is SetPrimaryAddressEvent) _setPrimaryAddressById(event.id);
    if (event is CreateNewPartnerEvent) _createNewPartner(event.locationModel);
    if (event is SuccessCreateNewPartnerEvent)
      yield SuccessCreateNewPartnerState(event.partnerData);
    if (event is DeletePartnerEvent) _deletePartner(event.parentId);
    if (event is SuccessDeletePartnerEvent)
      yield SuccessDeletePartnerState(event.partnerData);
    if (event is UpdatePartnerEvent)
      _updatePartner(event.parentId, event.locationModel, event.position);
    if (event is SuccessUpdateProfileEvent)
      yield SuccessUpdatePartnerState(event.partnerData);
    if (event is SuccessCreateNewPartnerEvent)
      yield SuccessCreateNewPartnerState(event.partnerData);

    if (event is SetFulNameEvent) _name = event.name;
    if (event is SetEmailEvent) _email = event.email;
    if (event is SetPhoneNumberEvent) _phone = event.phone;
    if (event is SetUserImageEvent) _userImage = event.userImage;
    if (event is SetCompanyNameEvent) _companyName = event.companyName;
    if (event is SetPrimaryAddressEvent)
      _handlePrimaryLocationStatus(event.id);
  }

  Future<void> _handlePrimaryLocationStatus(int id) async {
    try {
      showLoading();
      var partnerInfo = await _profileUseCase.updatePrimaryLocation(
          id, globalBloc.partnerData);
      globalBloc.partnerData.childIds.itemPartners
          .forEach((item) => item.primaryAddress = false);
      globalBloc.partnerData.childIds.itemPartners
          .firstWhere((item) => item.id == partnerInfo.id, orElse: () => null)
          ?.primaryAddress = partnerInfo.primaryAddress;
      add(SuccessProfileEvent(globalBloc.partnerData));
    } catch (ex) {
      showErrorDialog(localizationKey: ex.message);
    } finally {
      hideLoading();
    }
  }

  void _logout() async {
    try {
      showLoading();
      await _profileUseCase.clearAllUserData();
      add(SuccessLogoutEvent());
    } catch (ex) {
      showErrorDialog(localizationKey: ex.message);
    } finally {
      hideLoading();
    }
  }

  void _updateUserProfile(PartnerData partnerData) async {
    try {
      showLoading();
      var userInfo = await _profileUseCase.updateProfile(partnerData);
      globalBloc.partnerData = userInfo;
      add(SuccessProfileEvent(userInfo));
      showInfoDialog('profile_updated');
    } catch (ex) {
      showErrorDialog(localizationKey: ex.message);
    } finally {
      hideLoading();
    }
  }

  PartnerData _fillModelForUpdateProfile(PartnerData partnerData) {
    return PartnerData(
        id: partnerData.id,
        phoneValid: partnerData.phoneValid,
        name: (_name.isEmpty || _name == partnerData.name)
            ? partnerData.name
            : _name,
        street: partnerData.street,
        street2: partnerData.street2,
        zip: partnerData.zip,
        type: partnerData.type,
        lang: partnerData.lang,
        city: partnerData.city,
        phone: (_phone.isEmpty || _phone == partnerData.phone)
            ? partnerData.phone
            : _phone,
        mobile: partnerData.mobile,
        email: (_email.isEmpty || _email == partnerData.email)
            ? partnerData.email
        