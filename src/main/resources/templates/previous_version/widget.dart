import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:$main_package/core/bloc/bloc_action.dart';
import 'package:$main_package/app/navigation/app_navigator.dart';
import 'package:$main_package/app/navigation/navigate_action.dart';
import 'package:$main_package/core/ui/widgets/base_bloc_stateless_widget.dart';

import 'bloc/$file_name_bloc.dart';

class $name extends BaseBlocStatelessWidget<$nameBloc> {
  @override
  Widget build(BuildContext context) => Scaffold(
        body: SafeArea(
          child: _buildBody(context),
        ),
      );

  Widget _buildBody(BuildContext context) => BlocListener<$nameBloc, $nameState>(
        listenWhen: (previous, current) => previous.action != current.action,
        listener: (context, state) async {
          BlocAction? action = state.action;
          if (action is NavigateAction) {
            AppNavigator.navigate(context: context, action: action);
          }
        },
        child: Container(),
      );
}