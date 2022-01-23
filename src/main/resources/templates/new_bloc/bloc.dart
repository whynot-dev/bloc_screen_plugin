import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:$main_package/core/bloc/bloc_action.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part '$file_name_state.dart';

part '$file_name_event.dart';

part '$file_name_bloc.freezed.dart';

class $nameBloc extends Bloc<$nameEvent, $nameState> {
  $nameBloc() : super($nameState()) {
    on<Init>(_init);
  }

  FutureOr<void> _init(Init event, Emitter<$nameState> emit) {
    emit(state.copyWith());
  }
}
