import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:$main_package/core/ui/widgets/base_bloc_listener.dart';
import 'package:$main_package/core/ui/widgets/base_bloc_state_widget.dart';

import 'bloc/$file_name_bloc.dart';

class $name extends StatefulWidget {
  @override
  State<$name> createState() => _$nameState();
}

class _$nameState extends BaseBlocStateWidget<$name, $nameBloc, $nameEvent> {
  @override
  Widget build(BuildContext context) => Scaffold(
        body: SafeArea(
          child: _buildBody(context),
        ),
      );

  Widget _buildBody(BuildContext context) => BaseBlocListener<$nameBloc, $nameState>(
        listenWhen: (previous, current) => previous.action != current.action,
        listener: (context, state, action) async {},
        child: Container(),
      );

}